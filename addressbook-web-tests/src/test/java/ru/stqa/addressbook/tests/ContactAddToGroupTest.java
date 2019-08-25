package ru.stqa.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.addressbook.model.ContactData;
import ru.stqa.addressbook.model.Contacts;
import ru.stqa.addressbook.model.GroupData;
import ru.stqa.addressbook.model.Groups;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddToGroupTest extends TestBase {

    @BeforeMethod
    public void ensurePreconditions() {

        if (app.db().groups().size() == 0) {
            app.goTo().groupPage();
            app.group().create(new GroupData()
                    .withName("new1"));
        }
        Groups groups = app.db().groups();
        if (app.db().contacts().size() == 0) {
            app.goTo().homePage();
            app.contact().createContact(new ContactData()
                    .withFirstName("william")
                    .withLastName("Burroughs")
                    .withMiddleName("X")
                    .withMobilePhone("0")
                    .withEmail1("0")
                    .withGroup(groups.iterator().next()))
                    ;
                    }
        app.goTo().homePage();
    }
    @Test
    public void testContactAddToGroup() {
        Contacts contacts = app.db().contacts();
        Groups groups = app.db().groups();
        ContactData contact = contacts.iterator().next();
        int contactId = contact.getId();
        Groups contactGroupsBefore = contact.groups();
        while (groups.size() == 0){
            contact = contacts.iterator().next();
        }
        GroupData group = groups.stream().iterator().next();
        app.goTo().homePage();
        app.contact().addContactToGroup(contact, group);
        Contacts after = app.db().contacts();
        ContactData updatedContact = after.stream().filter(data -> Objects.equals(data.getId(), contactId)).findFirst().get();
        Groups newContactGroups = updatedContact.groups();
        assertThat(newContactGroups, equalTo(contactGroupsBefore.withAdded(group)));
    }

}