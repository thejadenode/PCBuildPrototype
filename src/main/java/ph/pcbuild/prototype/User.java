package ph.pcbuild.prototype;

import java.util.*;

import static org.apache.commons.lang3.Validate.*;

public class User {
    private final int userId;
    private final String firstName;
    private final String lastName;
    private HashMap cart = new HashMap<ComputerComponent, Integer>();

    User(int userId, String firstName, String lastName){
        isTrue(userId >= 0, "userId should be non-negative, was: " + userId);
        notBlank(firstName);
        notBlank(lastName);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getUserId(){ return userId;}


    public void addToCart(ComputerComponent component){
        notNull(component);
        component.lock();
        // make sure only one thread at a time
        try{
            component.decrementQuantity();
            Integer quantity = (Integer) cart.get(component) + 1;
            cart.put(component, quantity);
        } finally {
            component.unlock();
        }
    }

    public void removeFromCart(ComputerComponent component){
        notNull(component);
        component.lock();
        try{
            if(cart.remove(component)!= null){
                component.incrementQuantity();
                //might need to replace component in ArrayList Repo
            }
        } finally {
            component.unlock();
        }
    }

    public HashMap<ComputerComponent, Integer> getCart() {
        return new HashMap<ComputerComponent, Integer>(cart);
    }

    @Override
    public String toString() {
        return "User #" + userId + " " + firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
