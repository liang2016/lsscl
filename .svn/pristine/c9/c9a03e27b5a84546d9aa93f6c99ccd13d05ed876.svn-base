package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileOutputStream;

import com.serotonin.mango.web.mvc.form.ZoneLogoForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.serotonin.ShouldNeverHappenException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.view.RedirectView;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.User;

import com.serotonin.mango.web.mvc.SimpleFormRedirectController;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.scope.TradeVO;
import com.serotonin.mango.db.dao.scope.TradeDao;

public class SubZoneEditController extends SimpleFormRedirectController {
	private static final String SUBMIT_UPLOAD = "upload";
	private static final String SUBMIT_CLEAR_IMAGE = "clearImage";

	private String uploadDirectory;
	private int nextImageId = -1;

	public void setUploadDirectory(String uploadDirectory) {
		this.uploadDirectory = uploadDirectory;
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) throws Exception {
		ZoneLogoForm form = (ZoneLogoForm) command;

		if (hasSubmitParameter(request, SUBMIT_UPLOAD)) {
			//there is add a new scope  ，must be return
			if(form.getScope()==null||form.getScope().getId()==null)
				return;
			 if (form.getBackgroundImageMP() != null) {
				byte[] bytes = form.getBackgroundImageMP().getBytes();
				if (bytes != null && bytes.length > 0) {
					// Create the path to the upload directory.
					String path = request.getSession().getServletContext()
							.getRealPath(uploadDirectory);

					// Make sure the directory exists.
					File dir = new File(path);
					dir.mkdirs();

					// Get an image id.
					int imageId = getNextImageId(dir);

					// Create the image file name.
					String filename = Integer.toString(imageId);
					int dot = form.getBackgroundImageMP().getOriginalFilename()
							.lastIndexOf('.');
					if (dot != -1)
						filename += form.getBackgroundImageMP()
								.getOriginalFilename().substring(dot);

					// Save the file.
					FileOutputStream fos = new FileOutputStream(new File(dir,
							filename));
					fos.write(bytes);
					fos.close();
					//form.getScope().setId();
					form.getScope().setBackgroundFilename(
							uploadDirectory + filename);
					//there add data to database
					boolean update= new ScopeDao().updateScopeLogo(form.getScope().getId(),form.getScope().getBackgroundFilename());
					if(!update)
						return;
				}
				else{
					return;
				}
			}
		}

		if (hasSubmitParameter(request, SUBMIT_CLEAR_IMAGE)) {
			 form.getScope().setBackgroundFilename(null);
			 new ScopeDao().updateScopeLogo(form.getScope().getId(),null);
		}

	}
	@Override
    protected boolean isFormChangeRequest(HttpServletRequest request) {
        return hasSubmitParameter(request, SUBMIT_UPLOAD) || hasSubmitParameter(request, SUBMIT_CLEAR_IMAGE);
    }
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
            BindException errors) throws Exception {
    	  User user = Common.getUser(request);
        if (hasSubmitParameter(request, SUBMIT_UPLOAD))
            return getSuccessRedirectView("?zoneId="+user.getCurrentScope().getId());

        throw new ShouldNeverHappenException("Invalid submit parameter");
    }

	private int getNextImageId(File uploadDir) {
		if (nextImageId == -1) {
			// Synchronize
			synchronized (this) {
				if (nextImageId == -1) {
					// Initialize the next image id field.
					nextImageId = 1;

					String[] names = uploadDir.list();
					int index, dot;
					for (int i = 0; i < names.length; i++) {
						dot = names[i].lastIndexOf('.');
						try {
							if (dot == -1)
								index = Integer.parseInt(names[i]);
							else
								index = Integer.parseInt(names[i].substring(0,
										dot));
							if (index >= nextImageId)
								nextImageId = index + 1;
						} catch (NumberFormatException e) { /* no op */
						}
					}
				}
			}
		}
		return nextImageId++;
	}
}
