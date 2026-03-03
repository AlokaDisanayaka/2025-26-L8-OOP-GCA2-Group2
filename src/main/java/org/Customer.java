package org;
//Represents a Customer object and stores validated customer data (id, name, email, phone)
public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;

    public Customer(int customerId, String name, String email, String phone)
    {
        setCustomerId(customerId);
        setName(name);
        setEmail(email);
        setPhone(phone);
    }

    public Customer(String name, String email, String phone)
    {
        this(0, name, email, phone);
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        if (customerId < 0)
        {
            throw new IllegalArgumentException("customerId cannot be negative");
        }
        this.customerId = customerId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        this.name = name.trim();
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        //using a simple validation for emil
        if (email == null || email.trim().isEmpty() || !email.contains("@"))
        {
            throw new IllegalArgumentException("email must be valid");
        }
        this.email = email.trim();
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = (phone == null) ? null : phone.trim();
    }
}
