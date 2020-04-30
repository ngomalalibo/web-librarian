package com.pc.weblibrarian.codecs;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.pc.weblibrarian.entity.Address;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.conversions.Bson;

public class AddressCodec implements Codec<Address>
{
    @Override
    public Address decode(BsonReader bsonReader, DecoderContext decoderContext)
    {
        String objectId = bsonReader.readString();
        
        Bson query = Aggregates.match(Filters.eq("_id", objectId));
        JsonNode json;
//        Optional.ofNullable(Connection.addresses.aggregate(Collections.singletonList(query))).ifPresent(d ->  DocumentToJsonNodeConverter.documentToJsonNode( d.first()));
        
        
        //return Connection.addresses.find(query).iterator().tryNext().getCountry();
        return null;
        
    }
    
    @Override
    public void encode(BsonWriter bsonWriter, Address address, EncoderContext encoderContext)
    {
    
    }
    
    @Override
    public Class<Address> getEncoderClass()
    {
        return Address.class;
    }
}
