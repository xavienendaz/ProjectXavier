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
        name = "favoriteApi",
        version = "v1",
        resource = "favorite",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Xavier.example.com",
                ownerName = "backend.myapplication.Xavier.example.com",
                packagePath = ""
        )
)
public class FavoriteEndpoint {

    private static final Logger logger = Logger.getLogger(FavoriteEndpoint.class.getName());

    /**
     * This method gets the <code>Favorite</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Favorite</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getFavorite")
    public Favorite getFavorite(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getFavorite method");
        return null;
    }

    /**
     * This inserts a new <code>Favorite</code> object.
     *
     * @param favorite The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertFavorite")
    public Favorite insertFavorite(Favorite favorite) {
        // TODO: Implement this function
        logger.info("Calling insertFavorite method");
        return favorite;
    }
}