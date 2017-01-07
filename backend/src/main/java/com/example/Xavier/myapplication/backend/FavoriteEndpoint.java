package com.example.Xavier.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
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

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Favorite.class);
    }

    /**
     * Returns the {@link Favorite} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Favorite} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "favorite/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Favorite get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Favorite with ID: " + id);
        Favorite favorite = ofy().load().type(Favorite.class).id(id).now();
        if (favorite == null) {
            throw new NotFoundException("Could not find Favorite with ID: " + id);
        }
        return favorite;
    }

    /**
     * Inserts a new {@code Favorite}.
     */
    @ApiMethod(
            name = "insert",
            path = "favorite",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Favorite insert(Favorite favorite) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that favorite.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(favorite).now();
        logger.info("Created Favorite with ID: " + favorite.getId());

        return ofy().load().entity(favorite).now();
    }

    /**
     * Updates an existing {@code Favorite}.
     *
     * @param id       the ID of the entity to be updated
     * @param favorite the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Favorite}
     */
    @ApiMethod(
            name = "update",
            path = "favorite/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Favorite update(@Named("id") Long id, Favorite favorite) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(favorite).now();
        logger.info("Updated Favorite: " + favorite);
        return ofy().load().entity(favorite).now();
    }

    /**
     * Deletes the specified {@code Favorite}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Favorite}
     */
    @ApiMethod(
            name = "remove",
            path = "favorite/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Favorite.class).id(id).now();
        logger.info("Deleted Favorite with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "favorite",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Favorite> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Favorite> query = ofy().load().type(Favorite.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Favorite> queryIterator = query.iterator();
        List<Favorite> favoriteList = new ArrayList<Favorite>(limit);
        while (queryIterator.hasNext()) {
            favoriteList.add(queryIterator.next());
        }
        return CollectionResponse.<Favorite>builder().setItems(favoriteList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Favorite.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Favorite with ID: " + id);
        }
    }
}