package by.slesh.itechart.fullcontact.domain;


public class Entity {
    protected long id = -1;
    protected String value;

    public Entity() {
    }

    public Entity(long id, String value) {
	this.id = id;
	this.value = value;
    }

    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }
}
