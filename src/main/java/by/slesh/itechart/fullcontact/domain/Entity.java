package by.slesh.itechart.fullcontact.domain;


public abstract class Entity {
    protected Long id;
    protected String value;

    public Entity() {
    }

    public Entity(Long id, String value) {
	this.id = id;
	this.value = value;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }
}
