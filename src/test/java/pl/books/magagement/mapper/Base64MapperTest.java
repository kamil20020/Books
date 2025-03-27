package pl.books.magagement.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.books.magagement.model.mappers.Base64Mapper;
import pl.books.magagement.model.mappers.Base64MapperImpl;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class Base64MapperTest {

    private Base64Mapper base64Mapper = new Base64MapperImpl();

    @ParameterizedTest
    @CsvSource(value = {
        "raw, cmF3",
        "null, null",
    })
    public void shouldConvertBase64ToByteArray(String raw, String base64){

        //given
        if(raw.equals("null")){
            raw = null;
            base64 = null;
        }

        //when
        byte[] gotData = base64Mapper.base64ToByteArray(base64);
        String gotDataStr = raw != null ? new String(gotData, StandardCharsets.UTF_8) : null;

        //then
        assertEquals(raw, gotDataStr);
    }

    @Test
    public void shouldConvertByteArrayToBase64(){

        //given
        String raw = "raw";
        byte[] rawBytes = raw.getBytes(StandardCharsets.UTF_8);
        String base64 = "cmF3";

        //when
        String gotBase64 = base64Mapper.byteArrayToBase64(rawBytes);

        //then
        assertEquals(base64, gotBase64);
    }

    @Test
    public void shouldConvertByteArrayToBase64WhenNull(){

        //given

        //when
        String gotBase64 = base64Mapper.byteArrayToBase64(null);

        //then
        assertNull(gotBase64);
    }
}
