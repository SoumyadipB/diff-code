package com.ericsson.isf.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.isf.model.CommentModel;

@RestController
@RequestMapping("/comments")
public class CommentsController {
	
	List<CommentModel> listComments=new ArrayList<>();
	@RequestMapping(value = "/addComment", method = RequestMethod.POST)
	public CommentModel addComment(@RequestBody CommentModel commentModel) {
		return commentModel;
	}
	
	@RequestMapping(value = "/getComment", method = RequestMethod.POST)
	public CommentModel[] getComment() {
		return (CommentModel[]) listComments.toArray();
	}
	
	
	
}

