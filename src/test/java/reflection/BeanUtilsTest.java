package reflection;

import static org.junit.jupiter.api.Assertions.*;

class BeanUtilsTest {

    @org.junit.jupiter.api.Test
    void assignTestWithSuperclass() {
        Dog dog = new Dog();
        dog.setName("Bobby");
        dog.setSurname("The Second");
        dog.setTitle(true);
        Animal anotherDog = new Animal();
        BeanUtils.assign(anotherDog, dog);
        assertTrue(anotherDog.getTitle());
        assertEquals(anotherDog.getName(),"Bobby");
    }

    @org.junit.jupiter.api.Test
    void assignTestWithClass() {
        Dog dog = new Dog();
        dog.setName("Bobby");
        dog.setSurname("The Second");
        dog.setTitle(true);
        Dog anotherDog = new Dog();
        BeanUtils.assign(anotherDog, dog);
        assertTrue(anotherDog.getTitle());
        assertEquals(anotherDog.getName(),"Bobby");
    }

    class Animal {
        private String name;
        private boolean title;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setTitle(boolean title) {
            this.title = title;
        }

        public boolean getTitle() {
            return title;
        }
    }

    class Dog extends Animal {
        private String surname;

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }
}