package ru.stqa.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.addressbook.model.ContactData;
import ru.stqa.addressbook.model.Contacts;
import ru.stqa.addressbook.model.GroupData;
import ru.stqa.addressbook.model.Groups;

import java.security.acl.Group;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactRemovingFromGroupTest extends TestBase{

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
    public void testRemoveContactFromGroup() {
        Contacts contacts = app.db().contacts();
        ContactData removedContact = contacts.iterator().next();
        int contactId = removedContact.getId();
        Groups contactGroupsBefore = removedContact.groups();
        if (contactGroupsBefore.size() == 0) {
            Groups groups = app.db().groups();
            GroupData group = groups.stream().iterator().next();
            app.goTo().homePage();
            app.contact().addContactToGroup(removedContact, group);
            app.db().contacts();
        }

        contactGroupsBefore = removedContact.groups();
        GroupData group = contactGroupsBefore.iterator().next();
        app.goTo().homePage();
        app.contact().removeContactFromGroup(removedContact, group);

        Contacts after = app.db().contacts();
        ContactData updatedContact = after.stream().filter(data -> Objects.equals(data.getId(), contactId)).findFirst().get();
        Groups newContactGroups = updatedContact.groups();
        assertThat(newContactGroups, equalTo(contactGroupsBefore.without(group)));
    }
}