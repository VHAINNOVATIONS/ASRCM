package gov.va.med.srcalc.controller;

/**
 * Temporary kludge until I figure out how to dynamically populate and validate
 * variable values.
 */
public class SubmittedValues
{
    public int age;
    public String gender;

    public int getAge()
    {
        return age;
    }
    public void setAge(int age)
    {
        this.age = age;
    }
    public String getGender()
    {
        return gender;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }
}
