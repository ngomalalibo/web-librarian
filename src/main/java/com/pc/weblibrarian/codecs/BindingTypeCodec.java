package com.pc.weblibrarian.codecs;

import com.pc.weblibrarian.enums.BindingType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class BindingTypeCodec implements Codec<BindingType>
{
    @Override
    public BindingType decode(BsonReader bsonReader, DecoderContext decoderContext)
    {
        return null;
    }
    
    @Override
    public void encode(BsonWriter bsonWriter, BindingType bindingType, EncoderContext encoderContext)
    {
    
    }
    
    @Override
    public Class<BindingType> getEncoderClass()
    {
        return null;
    }
}
