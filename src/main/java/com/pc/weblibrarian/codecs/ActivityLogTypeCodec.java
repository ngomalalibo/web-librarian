package com.pc.weblibrarian.codecs;

import com.pc.weblibrarian.enums.ActivityLogType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class ActivityLogTypeCodec implements Codec<ActivityLogType>
{
    @Override
    public ActivityLogType decode(BsonReader bsonReader, DecoderContext decoderContext)
    {
        return ActivityLogType.fromValue(bsonReader.readString());
    }
    
    @Override
    public void encode(BsonWriter bsonWriter, ActivityLogType activityLog, EncoderContext encoderContext)
    {
        bsonWriter.writeString(activityLog.getValue());
    }
    
    @Override
    public Class<ActivityLogType> getEncoderClass()
    {
        return ActivityLogType.class;
    }
}
