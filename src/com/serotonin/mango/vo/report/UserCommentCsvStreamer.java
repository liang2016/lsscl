/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;

import com.serotonin.mango.view.export.CsvWriter;
import com.serotonin.mango.vo.UserComment;
import com.serotonin.web.i18n.I18NUtils;

/**
 *  
 */
public class UserCommentCsvStreamer {
    public UserCommentCsvStreamer(PrintWriter out, List<ReportUserComment> comments, ResourceBundle bundle) {
        CsvWriter csvWriter = new CsvWriter();
        String[] data = new String[5];

        // Write the headers.
        data[0] = I18NUtils.getMessage(bundle, "users.username");
        data[1] = I18NUtils.getMessage(bundle, "reports.commentList.type");
        data[2] = I18NUtils.getMessage(bundle, "reports.commentList.typeKey");
        data[3] = I18NUtils.getMessage(bundle, "reports.commentList.time");
        data[4] = I18NUtils.getMessage(bundle, "notes.note");
        out.write(csvWriter.encodeRow(data));

        for (ReportUserComment comment : comments) {
            data[0] = comment.getUsername();
            if (data[0] == null)
                data[0] = I18NUtils.getMessage(bundle, "common.deleted");
            if (comment.getCommentType() == UserComment.TYPE_EVENT) {
                data[1] = I18NUtils.getMessage(bundle, "reports.commentList.type.event");
                data[2] = Integer.toString(comment.getTypeKey());
            }
            else if (comment.getCommentType() == UserComment.TYPE_POINT) {
                data[1] = I18NUtils.getMessage(bundle, "reports.commentList.type.point");
                data[2] = comment.getPointName();
            }
            else {
                data[1] = I18NUtils.getMessage(bundle, "common.unknown");
                data[2] = "";
            }

            data[3] = comment.getPrettyTime();
            data[4] = comment.getComment();

            out.write(csvWriter.encodeRow(data));
        }

        out.flush();
        out.close();
    }
}
