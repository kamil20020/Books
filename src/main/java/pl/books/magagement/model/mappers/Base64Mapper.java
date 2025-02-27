package pl.books.magagement.model.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Base64;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface Base64Mapper {

    @Named(value = "byteArrayToBase64")
    default String byteArrayToBase64(byte[] rawData){

        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(rawData);
    }

    @Named(value = "base64ToByteArray")
    default byte[] base64ToByteArray(String encodedData){

        Base64.Decoder decoder = Base64.getDecoder();

        return decoder.decode(encodedData);
    }
}
