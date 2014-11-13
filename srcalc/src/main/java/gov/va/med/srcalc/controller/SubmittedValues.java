package gov.va.med.srcalc.controller;

/**
 * Temporary kludge until I figure out how to dynamically populate and validate
 * variable values.
 */
public class SubmittedValues
{
    private int age;
    private String gender;

    public int getAge()
    {
        return age;
    }
    public void setAge(final int age)
    {
        this.age = age;
    }
    public String getGender()
    {
        return gender;
    }
    public void setGender(final String gender)
    {
        this.gender = gender;
    }
}
