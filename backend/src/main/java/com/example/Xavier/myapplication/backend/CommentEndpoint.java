package com.example.Xavier.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "commentApi",
        version = "v1",
        resource = "comment",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Xavier.example.com",
                ownerName = "backend.myapplication.Xavier.example.com",
                packagePath = ""
        )
)
public class CommentEndpoint {

    private static final Logger logger = Logger.getLogger(CommentEndpoint.class.getName());

    /**
     * This method gets the <code>Comment</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Comment</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getComment")
    public Comment getComment(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getComment method");
        return null;
    }

    /**
     * This inserts a new <code>Comment</code> object.
     *
     * @param comment The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertComment")
    public Comment insertComment(Comment comment) {
        // TODO: Implement this function
        logger.info("Calling insertComment method");
        return comment;
    }
}