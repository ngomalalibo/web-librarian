package com.pc.weblibrarian.codecs.models;

import com.mongodb.client.model.Filters;
import com.pc.weblibrarian.dataService.Connection;
import com.pc.weblibrarian.model.LibraryItemLocation;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * Un-used Class
 */

public class LibraryItemLocationCodec implements Codec<LibraryItemLocation>
{
    @Override
    public LibraryItemLocation decode(BsonReader bsonReader, DecoderContext decoderContext)
    {
        ObjectId objectId = bsonReader.readObjectId();
        
        Bson query = Filters.eq("_id", objectId);
        
        return Connection.libraryItem.find(query).iterator().tryNext().getLibraryItemLocation();
        
        
    }
    
    @Override
    public void encode(BsonWriter bsonWriter, LibraryItemLocation libraryItemLocation, EncoderContext encoderContext)
    {
    
    }
    
    @Override
    public Class<LibraryItemLocation> getEncoderClass()
    {
        return LibraryItemLocation.class;
    }
}
