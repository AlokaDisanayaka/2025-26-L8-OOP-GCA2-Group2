package org.DTO;
//Represents a Customer object and stores validated customer data (id, name, email, phone)
public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;

    public Customer(int customerId, String name, String email, String phone)
    {
        this.customerId=customerId;
        this.name=name;
        this.email=email;
        this.phone=phone;
    }
    public Customer()
    {

    }


    public Customer(String name, String email, String phone)
    {
        this.customerId=0;
        this.name=name;
        this.email=email;
        this.phone=phone;

    }

    public int getCustomerId()
    {
        return this.customerId;
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
        return this.name;
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
        return this.email;
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
        return this.phone;
    }

    public void setPhone(String phone)
    {
        this.phone = (phone == null) ? null : phone.trim();
    }
}
