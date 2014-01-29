package info.tregmine.api.returns;

public class BooleanStringReturn
{
    private boolean value_boolean;
    private String value_string;

    public BooleanStringReturn(boolean one, String two)
    {
        this.value_boolean = one;
        this.value_string = two;
    }

    public boolean getBoolean()
    {
        return this.value_boolean;
    }

    public String getString()
    {
        return this.value_string;
    }
}
