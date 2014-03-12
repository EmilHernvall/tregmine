package info.tregmine.api.bank;

/*
 * Banker_Names is a basic enum for establish a randomised banker name on creation of a banker
 * Names of bankers will be editable by players from a cost, this is only for the initial creation.
 */
public enum Banker_Names {

    // 10 Male, 10 Female
    EARL("Earl"),
    BEN("Ben"),
    JOHN("John"),
    PETER("Peter"),
    WALTER("Walter"),
    GUSTAVO("Gustavo"),
    CARL("Carl"),
    JAMES("James"),
    JIMMY("Jimmy"),
    ROBERT("Robert"),
    KELLY("Kelly"),
    DEANNA("Deanna"),
    SUSAN("Susan"),
    EMMA("Emma"),
    BETH("Beth"),
    CHLOE("Chloe"),
    TRACY("Tracy"),
    DIANE("Diane"),
    HANNAH("Hannah"),
    SOPHIE("Sophie");

    private String name;
    private Banker_Names(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
