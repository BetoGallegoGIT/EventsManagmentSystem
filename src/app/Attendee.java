package app;

public class Attendee {

    // ----------------------------------------------------------------------------- //

    private String name;
    private String email;
    private long dni;
    private long phone;

    // ----------------------------------------------------------------------------- //

    public Attendee(String name, String email, long dni, long phone) {
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
    }

    // ----------------------------------------------------------------------------- //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    // ----------------------------------------------------------------------------- //

    @Override
    public String toString() {
        return this.name + " | " + this.email + " | DNI: " + this.dni + " | Phone number: " + this.phone;
    }

    // ----------------------------------------------------------------------------- //

}
