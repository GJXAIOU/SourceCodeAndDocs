package java.lang;


/**
 * @author GJXAIOU
 * @create 2019-09-17-20:01
 */

class Person {
    private String name;
    private int age;

    public Person(){
    }
    public Person(String name, int age) {
        name = this.name;
        age = this.age;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        name = this.name;
    }
    public int getAge(){
        return age;
    }
    public void setAge(int age){
        age = this.age;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if(o == null || !(o instanceof Person)){
            return false;
        }
        Person person = (Person)o;
        if ((person.getName().equals(this.getName())) &&(person.getAge()==(this.getAge()))){
            return true;
        }
        return false;

    }
}
public class ObjectEqualsTest {
    public static void main(String[] args) {
        Person person1 = new Person("zhangsan", 18);
        Person person2 = new Person("zhangsan", 18);
        Person person3 = new Person("lisi", 18);

        System.out.println(person1.equals(person2));
        System.out.println(person1.equals(person3));
    }
}


