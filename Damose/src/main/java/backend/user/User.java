package main.java.backend.user;

public class User {
	
	private String id, userName, hashedPassword;
	
	public User(String id, String name, String hashedPassword) {
		
		this.id = id;
		this.userName = name;
		this.hashedPassword = hashedPassword;
	}
	
	public static User fromPlainPassword(String id, String userName, String plainPassword) {
        String hash = PasswordUtil.hash(plainPassword);
        return new User(id, userName, hash);
    }
	
	public String getUserName() {
		
		return this.userName;
	}
	
    public boolean checkPassword(String plainPassword) {
        return PasswordUtil.hash(plainPassword).equals(hashedPassword);
    }
	
	public String getId() {
		
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof User)) return false;
	        User user = (User) o;
	        return id.equals(user.id);
	}
	
	@Override
    public int hashCode() {
        return id.hashCode();
    }

}
