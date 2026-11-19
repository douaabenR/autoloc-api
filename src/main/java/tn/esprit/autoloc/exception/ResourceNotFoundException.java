package tn.esprit.autoloc.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " avec l'identifiant " + id + " introuvable.");
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(resourceName + " « " + identifier + " » introuvable.");
    }
}
