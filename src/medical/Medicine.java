package medical;

public class Medicine {
    private String name;
    private String use;

    public Medicine(String name, String use) {
        this.name = name;
        this.use = use;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    @Override
    public String toString() {
        return "Medicine {" +
                "name='" + name + '\'' +
                ", use='" + use + '\'' +
                '}';
    }
}
