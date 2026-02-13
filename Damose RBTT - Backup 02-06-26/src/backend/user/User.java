package backend.user;

/**
 * The Class User -> represents a user object in the user database.
 */
public class User {
	
	/** The user id, the user name and the hashed password. */
	private String id, userName, hashedPassword;
	
	/**
	 * Instantiates a new user.
	 *
	 * @param id the id
	 * @param name the name
	 * @param hashedPassword the hashed password
	 */
	public User(String id, String name, String hashedPassword) {
		
		this.id = id;
		this.userName = name;
		this.hashedPassword = hashedPassword;
	}
	
	/**
	 * Creates a user by inserting a plain password, which will be eventually hashed.
	 *
	 * @param id the id
	 * @param userName the user name
	 * @param plainPassword the plain password
	 * @return the user
	 */
	public static User fromPlainPassword(String id, String userName, String plainPassword) {
        String hash = PasswordUtil.hash(plainPassword);
        return new User(id, userName, hash);
    }
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		
		return this.userName;
	}
	
    /**
     * Checks if the input password equals to the user's password.
     *
     * @param plainPassword the plain password
     * @return true, if successful
     */
    public boolean checkPassword(String plainPassword) {
        return PasswordUtil.hash(plainPassword).equals(hashedPassword);
    }
	
	/**
	 * Gets the user id.
	 *
	 * @return the id
	 */
	public String getId() {
		
		return this.id;
	}
	
	/**
	 * Equals.
	 *
	 * @param o the o
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof User)) return false;
	        User user = (User) o;
	        return id.equals(user.id);
	}
	
	/**
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
    public int hashCode() {
        return id.hashCode();
    }

}
