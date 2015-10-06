package com.java.doc.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.java.doc.model.BookSendOut;
import com.java.doc.service.BookSendOutService;
import com.java.doc.service.DivisionService;
import com.java.doc.service.TypeQuickService;
import com.java.doc.service.TypeSecretService;
import com.java.doc.validator.BookSendOutValidator;

@Controller
public class ExternalController {

	private TypeQuickService typeQuick;
	private TypeSecretService typeSecret;
	private DivisionService divisions;
	private BookSendOutService sendout;

	@Autowired(required = true)
	@Qualifier(value = "sendoutservice")
	public void setSendout(BookSendOutService sendout) {
		this.sendout = sendout;
	}

	@Autowired(required = true)
	@Qualifier(value = "divisionService")
	public void setDivisions(DivisionService divisions) {
		this.divisions = divisions;
	}

	@Autowired(required = true)
	@Qualifier(value = "TypeSecretService")
	public void setTypeSecret(TypeSecretService typeSecret) {
		this.typeSecret = typeSecret;
	}

	@Autowired(required = true)
	@Qualifier(value = "TypeQuickService")
	public void setTypeQuick(TypeQuickService typeQuick) {
		this.typeQuick = typeQuick;
	}
	

	@RequestMapping(value = "addexternal", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
	public ModelAndView AddExternal() throws ParseException {
		ModelAndView model = new ModelAndView();
		model.addObject("sendOut", new BookSendOutValidator());
		model.addObject("quick", this.typeQuick.listTypeQuick());
		model.addObject("secret", this.typeSecret.listTypeSecret());
		model.addObject("divisions", this.divisions.selectDivision());
		Locale locale = new Locale("th", "TH");
		Locale.setDefault(locale);
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault());
		String d = format.format(new Date());
		model.addObject("date", d);
		model.addObject("lastId", this.sendout.LastID());
		model.setViewName("external");
		return model;
	}
	
	@RequestMapping(value = "/saveSendOut", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody String post(@ModelAttribute("sendOutForm") BookSendOutValidator pet) {
		BookSendOut out = sendout.convert(pet);
		sendout.saveBookOut(out);
		
		return "a";
	}
	
	@RequestMapping(value = "/external/upload", method = RequestMethod.POST)
	@PreAuthorize("isAuthenticated()")
	public @ResponseBody
    String uploadMultipleFileHandler(@RequestParam("name") String[] names,
            @RequestParam("file") MultipartFile[] files) {
 
        if (files.length != names.length)
            return "Mandatory information missing";
        Path currentDir = Paths.get(".");
        System.out.println(currentDir.toAbsolutePath());
        String message = "";
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String name = names[i];
            try {
                byte[] bytes = file.getBytes();
                File dir = new File("D:\\workspaceJava\\SpringDoc\\src\\main\\webapp\\resources\\external");
                if (!dir.exists())
                    dir.mkdirs();
 
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
 
                message = message + "You successfully uploaded file=" + name
                        + "<br />";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        }
        return message;
    }
}
