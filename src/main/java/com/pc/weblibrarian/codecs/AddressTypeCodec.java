package com.pc.weblibrarian.codecs;

import com.pc.weblibrarian.enums.AddressType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class AddressTypeCodec implements Codec<AddressType>
{
    @Override
    public AddressType decode(BsonReader bsonReader, DecoderContext decoderContext)
    {
        return AddressType.fromValue(bsonReader.readString());
    }
    
    @Override
    public void encode(BsonWriter bsonWriter, AddressType addressType, EncoderContext encoderContext)
    {
        bsonWriter.writeString(addressType.value());
    }
    
    @Override
    public Class<AddressType> getEncoderClass()
    {
        return AddressType.class;
    }
}
