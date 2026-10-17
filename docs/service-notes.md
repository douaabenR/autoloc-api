# Couche Service AutoLoc

## A. Modes d'injection

| Mode | Utilisation | Décision pour AutoLoc |
|---|---|---|
| Constructeur | Les dépendances sont fournies à la création de l'objet; les champs peuvent être `final`. | Retenu partout avec `@RequiredArgsConstructor`. Les services dépendent des interfaces repository et les contrôleurs des interfaces service. |
| Setter | La dépendance peut être remplacée après la création; adapté à une dépendance réellement optionnelle. | Non utilisé : repositories et services sont nécessaires au fonctionnement des composants. |
| Attribut (`@Autowired`) | Spring injecte directement dans un champ, qui reste difficile à initialiser hors du conteneur. | Écarté. SonarQube for IDE signale ce style avec S6813; le code final conserve l'injection par constructeur. |

IoC signifie que le conteneur Spring contrôle la création et l'assemblage des objets. La DI est le mécanisme qui fournit une dépendance à un composant. Les beans sont détectés dans `tn.esprit.autoloc` et ses sous-packages. Les services sont des singletons par défaut : ils ne gardent donc aucun état lié à une requête. Les entités JPA ne sont pas des beans Spring.

`@Service`, `@Repository` et `@Controller` spécialisent `@Component`. `@Bean` déclare explicitement un objet dans une classe `@Configuration`. `@Primary` ou `@Qualifier` permet de choisir entre plusieurs beans compatibles.

## B. Services livrés

Tous les services CRUD sont `@Service`, utilisent `@RequiredArgsConstructor`, possèdent un repository `final` et appliquent leurs validations à la création et à la mise à jour. Les recherches par identifiant et suppressions lèvent `ResourceNotFoundException` lorsque la ressource manque. Les contrôleurs injectent les interfaces service par constructeur.

| Interface / implémentation | Repository | Opérations | Règle métier |
|---|---|---|---|
| `IAgenceService` / `AgenceServiceImpl` | `IAgenceRepository` | CRUD | Le nom est obligatoire et non vide. |
| `IEmployeService` / `EmployeServiceImpl` | `IEmployeRepository` | CRUD | Le nom et le poste (champ `role` dans le modèle) sont obligatoires. |
| `IVehiculeService` / `VehiculeServiceImpl` | `IVehiculeRepository` | CRUD | Le tarif journalier est obligatoire et non négatif. |
| `IEquipementService` / `EquipementServiceImpl` | `IEquipementRepository` | CRUD | Libellé obligatoire; quantité non négative. |
| `IClientService` / `ClientServiceImpl` | `IClientRepository` | CRUD | Nom et e-mail obligatoires. |
| `IReservationService` / `ReservationServiceImpl` | `IReservationRepository` | CRUD | Dates obligatoires; fin égale ou postérieure au début. |
| `IContratService` / `ContratServiceImpl` | `IContratRepository` | CRUD | Identifiant interdit à la création; montant non négatif; mise à jour sans remplacer les paiements. La création synchronise le côté propriétaire de la relation et conserve la cascade JPA. |
| `IPaiementService` / `PaiementServiceImpl` | `IPaiementRepository` | Lecture : `findById`, `findAll` | Aucun service ni contrôleur ne propose de création, modification ou suppression autonome. |
| `IMaintenanceService` / `MaintenanceServiceImpl` | `IMaintenanceRepository` | CRUD | Coût non négatif; date de fin facultative, mais si présente elle ne précède pas le début. |

Les contrôleurs sont sous `/api/agences`, `/api/employes`, `/api/vehicules`, `/api/equipements`, `/api/clients`, `/api/reservations`, `/api/contrats`, `/api/paiements` et `/api/maintenances`. Les erreurs de validation renvoient HTTP 400; une ressource absente renvoie HTTP 404. Les identifiants créés sont générés par JPA.

Le modèle ne possédait pas de quantité pour `Equipement` ni de coût pour `Maintenance`; les champs `quantite` et `cout` ont été ajoutés afin que les règles demandées soient exprimables et persistées.

## C. Diagnostic des erreurs de démarrage

| Message | Cause probable | Diagnostic / correction |
|---|---|---|
| `NoSuchBeanDefinitionException` | Aucun bean du type demandé, annotation absente ou classe hors du scan. | Vérifier `@Service`/`@Component`, l'interface et le package sous `tn.esprit.autoloc`. |
| `NoUniqueBeanDefinitionException` | Plusieurs implémentations du même type sont candidates. | Choisir avec `@Primary` sur l'implémentation ou `@Qualifier` sur le paramètre du constructeur. |
| `UnsatisfiedDependencyException` | Erreur enveloppe indiquant qu'une dépendance n'a pas pu être résolue. | Lire le dernier `Caused by` pour retrouver l'erreur racine. |
| Cycle entre services | Deux services requièrent chacun l'autre au constructeur. | Extraire la responsabilité commune dans un troisième service et supprimer le cycle. |

Avec l'injection par constructeur, un cycle empêche la création des deux beans : aucun ne peut être construit en premier.

## D. Revue SonarQube et anomalies corrigées

| Point | Règle / constat | Correction |
|---|---|---|
| Injection directe dans un champ lors de la comparaison pédagogique | S6813 | La variante par attribut est temporaire; le code livré utilise des champs `final` injectés au constructeur. |
| Remplacement temporaire de l'exception métier par `RuntimeException` | S112 | Le code livré restaure `ResourceNotFoundException`, dédiée aux ressources absentes. Les entrées métier invalides utilisent `IllegalArgumentException` comme demandé. |
| `IEmployeRepository` ne prolongeait aucun repository Spring Data | Le bean repository Employe ne pouvait pas être créé. | L'interface prolonge désormais `JpaRepository<Employe, Long>`. |
| Les règles de quantité et de coût ne correspondaient pas au modèle JPA existant | Les champs nécessaires manquaient aux entités. | Ajout de `Equipement.quantite` et `Maintenance.cout`, puis validations dans les services. |
| Relations JPA bidirectionnelles exposées directement par REST | Risque de récursion à la sérialisation JSON. | `@JsonIdentityInfo` identifie les objets dans le graphe JSON. |

Les lignes S6813 et S112 décrivent les vérifications temporaires demandées dans l'exercice; elles ne sont pas conservées dans le code final.

## E. Questions de compréhension

1. **Quelle différence entre IoC et DI ?** IoC décrit le contrôle de création confié à Spring; DI est le mécanisme par lequel Spring fournit les dépendances.
2. **Pourquoi préférer l'injection par constructeur ?** Elle rend les dépendances visibles et obligatoires, permet des champs `final` et facilite les tests sans conteneur Spring.
3. **Pourquoi un service singleton doit-il rester sans état ?** Plusieurs requêtes partagent la même instance; un état mutable par requête pourrait être lu ou modifié par une autre requête.
4. **Comment résoudre une ambiguïté entre deux beans du même type ?** Ajouter `@Primary` à une implémentation ou un `@Qualifier` au point d'injection.
5. **Pourquoi mettre à jour un contrat chargé avant de le sauvegarder ?** Cela préserve l'identité et les champs non concernés, notamment la collection de paiements et sa relation en cascade.
