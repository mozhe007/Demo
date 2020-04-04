package commonbean;

/**
 * 普通的Object
 */
public class Bean {

    private String id;
    public Bean() {
    }
    public Bean(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return id;
    }

}
