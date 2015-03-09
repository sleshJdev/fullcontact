package by.slesh.itechart.fullcontact.template;

import java.sql.Date;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import by.slesh.itechart.fullcontact.domain.ContactEntity;
import by.slesh.itechart.fullcontact.util.PathUtil;

public class EmailTemplate {
    public static String build(EmailTemplate.Parameters parameters, String root, String templateName) {

	StringTemplateGroup group = new StringTemplateGroup(null, root, DefaultTemplateLexer.class);
	StringTemplate template = group.getInstanceOf(templateName);
	template.setAttribute("TITLE", parameters.getTitle());
	template.setAttribute("NAME", parameters.getName());
	template.setAttribute("MESSAGE_BODY", parameters.getMessageBody());
	template.setAttribute("US_FULL_NAME", parameters.getUsFullName());
	template.setAttribute("US_PHONE", parameters.getUsPhone());
	template.setAttribute("US_EMAIL", parameters.getUsEmail());

	System.out.println(template.toString());

	return null;
    }

    public static void main(String[] args) {
	ContactEntity contact = new ContactEntity();
	contact.setFirstName("Test1First");
	contact.setLastName("Test1Last");
	contact.setMiddleName("Test1Middle");
	contact.setDateOfBirth(new Date(new java.util.Date().getTime()));
	contact.setSex("Male");
	contact.setNationality("Belarus");
	contact.setFamilyStatus("Divorced");
	contact.setCurrentEmployment("Test1CureentEmployment");
	contact.setWebSite("test1@mail.ru");
	contact.setEmailAddress("test1@mail.ru");
	contact.setCountry("Test1Country");
	contact.setCity("Test1City");
	contact.setStreet("Street1");
	contact.setHouse("13Houes");
	contact.setBlock("Block1");
	contact.setApartment("Apa12321");
	contact.setCityIndex("123490");

	EmailTemplate template = new EmailTemplate();
	EmailTemplate.Parameters parameters = template.new Parameters();
	parameters.setMessageBody("I have notify, that you noob!");
	parameters.setName(contact.getFirstName());
	parameters.setTitle("Congratulation");
	parameters.setUsFullName(String.format("%s %s %s", "Some", "Blah", "Todo"));
	parameters.setUsEmail("slesh.eee");
	parameters.setUsPhone("12312312312312");

	String root = PathUtil.getResourceFile("templates").getPath();
	String name = "congratulation";
	EmailTemplate.build(parameters, root, name);
    }

    public class Parameters {
	private String title;
	private String name;
	private String messageBody;
	private String usFullName;
	private String usPhone;
	private String usEmail;

	public Parameters() {
	}

	public Parameters(String title, String name, String messageBody, String usFullName, String usPhone,
		String usEmail) {
	    super();
	    this.title = title;
	    this.name = name;
	    this.messageBody = messageBody;
	    this.usFullName = usFullName;
	    this.usPhone = usPhone;
	    this.usEmail = usEmail;
	}

	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
	    this.title = title;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getMessageBody() {
	    return messageBody;
	}

	public void setMessageBody(String messageBody) {
	    this.messageBody = messageBody;
	}

	public String getUsFullName() {
	    return usFullName;
	}

	public void setUsFullName(String usFullName) {
	    this.usFullName = usFullName;
	}

	public String getUsPhone() {
	    return usPhone;
	}

	public void setUsPhone(String usPhone) {
	    this.usPhone = usPhone;
	}

	public String getUsEmail() {
	    return usEmail;
	}

	public void setUsEmail(String usEmail) {
	    this.usEmail = usEmail;
	}

    }
}
