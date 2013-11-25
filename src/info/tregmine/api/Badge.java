package info.tregmine.api;

public enum Badge
{
    MENTOR("Mentor"),
    MERCHANT("Merchant"),
    GOOD_CITIZEN("Good citizen"),
    PHILANTROPIST("Philantropist"),
    PROSPECTOR("Prospector"),
    FOUNDER("Founder"),
    CHAMPION("Champion"),
    ADDICT("Addict"),
    CRAFTMAN("Craftman"),
    INVINCIBLE("Invincible");

    private String name;

    private Badge(String name)
    {
        this.name = name;
    }

    public String getName() { return name; }

    public static Badge fromString(String str)
    {
        for (Badge badge : Badge.values()) {
            if (str.equalsIgnoreCase(badge.toString())) {
                return badge;
            }
        }

        return null;
    }
}
