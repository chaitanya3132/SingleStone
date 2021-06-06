package contactentrysystem.demo.contacts.controller;

import contactentrysystem.demo.contacts.model.*;
import contactentrysystem.demo.contacts.service.ContactService;
import contactentrysystem.demo.contacts.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class ContactController {

    @Autowired
    private ContactService contactService;
    @Autowired
    private PhoneService phoneService;


    public ContactController() {}

    @RequestMapping(value = "/contacts",method = RequestMethod.POST)
    public String createContact(@RequestBody Contact contact) {

        contact.getPhone().forEach(phone -> {phone.setContact(contact);});
        contactService.createContact(contact);
        return "Contact Created succesfully";
    }

    @RequestMapping(value = "/contacts/{id}",method = RequestMethod.DELETE)
    public String deleteContact(@PathVariable("id") Long id) {
        contactService.deleteContactById(id);
        return "Contact Deleted Successfully";
    }

    @RequestMapping(value = "/contacts/{id}",method = RequestMethod.PUT)
    public String updateContact(@PathVariable("id") Long id,@RequestBody Contact contact) {
        contact.getPhone().forEach(phone -> {phone.setContact(contact);});
        contactService.updateContactById(id, contact);
        return "Contact Updated Successfully";
    }

    @RequestMapping(value = "/contacts/{id}",method = RequestMethod.GET)
    public Contact getContact(@PathVariable("id") Long id) {
        return contactService.getContactById(id);

    }

    @RequestMapping(value = "/contacts",method = RequestMethod.GET)
    public List<Contact> getAllContacts() {

        return contactService.getContacts();
    }

    @RequestMapping(value = "/contacts/call-list",method = RequestMethod.GET)
    public List<callList> getCallList() {

//        List<Contact> response = contactService.getCallList();

        return contactService.getCallList().stream()
//                .filter(x -> x.getPhone().stream()
//                        .anyMatch(y -> y.getType() == Type.home))
                .map(c -> {
                    Name name = Name.builder()
                            .first(c.getName().getFirst())
                            .middle(c.getName().getMiddle())
                            .last(c.getName().getLast()).build();

                    Phone phone = c.getPhone().stream()
                            .filter(p -> p.getType() == Type.home).findFirst().get();

                    callList calllist = callList.builder()
                            .name(name)
                            .Number(phone.getNumber())
                            .build();

                    return calllist;
                } )
                .collect(Collectors.toList());


//        List<callList> final_response = new ArrayList<>();
//        for (Contact contact : response) {
//                 Name name = Name.builder()
//                         .first(contact.getName().getFirst())
//                         .middle(contact.getName().getMiddle())
//                         .last(contact.getName().getLast()).build();
//                 for (Phone p: contact.getPhone()){
//                     if (p.getType() == Type.home) {
//                         callList calllist = callList.builder()
//                                 .name(name)
//                                 .Number(p.getNumber())
//                                 .build();
//                         final_response.add(calllist);
//                     }
////                     else{
////                         continue;
////                     }
//
//             }
//
//
//        }
//        return final_response;
    }



}
