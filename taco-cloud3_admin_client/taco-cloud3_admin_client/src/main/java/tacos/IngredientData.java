package tacos;

public class IngredientData {

    private String id;
    private String name;
    private Ingredient.Type type;
    private String username;
    // No need for appUserId here as it will be fetched from the logged-in user

    // Constructors, getters, and setters
    public IngredientData() {}

    public IngredientData(String id, String name, Ingredient.Type type, String username) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.username = username; // Initialize username
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ingredient.Type getType() {
        return type;
    }

    public void setType(Ingredient.Type type) {
        this.type = type;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
