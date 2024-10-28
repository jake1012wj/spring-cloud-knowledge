package com.jeesite.modules.blog.picture.form;

import com.jeesite.modules.blog.base.vo.FileVO;
import lombok.Data;

@Data
public class SearchPictureForm extends FileVO {
    private String searchKey;
    private Integer count;
}
