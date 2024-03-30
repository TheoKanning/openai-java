package io.github.panghy.openai.service;

import io.github.panghy.openai.client.OpenAiApi;
import io.github.panghy.openai.image.*;
import io.github.panghy.openai.service.OpenAiService;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static io.reactivex.Single.just;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


public class ImageTest {

    static String filePath = "src/test/resources/penguin.png";
    static String fileWithAlphaPath = "src/test/resources/penguin_with_alpha.png";
    static String maskPath = "src/test/resources/mask.png";

    OpenAiApi mockApi;
    OpenAiService service;

    @BeforeEach
    void setUp() {
        mockApi = mock(OpenAiApi.class);
        service = new OpenAiService(mockApi);
    }

    @Test
    void createImageUrl() {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt("penguin")
                .n(3)
                .size("256x256")
                .user("testing")
                .build();

        when(mockApi.createImage(createImageRequest)).thenReturn(just(ImageResult.builder()
            .data(List.of(
                Image.builder().url("https://openai.com/penguin.png").build(),
                Image.builder().url("https://openai.com/penguin2.png").build(),
                Image.builder().url("https://openai.com/penguin3.png").build()))
            .build()));
        List<Image> images = service.createImage(createImageRequest).getData();
        verify(mockApi, times(1)).createImage(createImageRequest);
        assertEquals(3, images.size());
        assertNotNull(images.get(0).getUrl());
    }

    @Test
    void createImageBase64() {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt("penguin")
                .responseFormat("b64_json")
                .user("testing")
                .build();

        when(mockApi.createImage(createImageRequest)).thenReturn(just(ImageResult.builder().
            data(List.of(Image.builder().
                b64Json("data:image/png;base64,abcdefg")
                .build()))
            .build()));
        List<Image> images = service.createImage(createImageRequest).getData();
        verify(mockApi, times(1)).createImage(createImageRequest);
        assertEquals(1, images.size());
        assertNotNull(images.get(0).getB64Json());
    }

    @Test
    void createImageEdit() {
        CreateImageEditRequest createImageRequest = CreateImageEditRequest.builder()
                .prompt("a penguin with a red background")
                .responseFormat("url")
                .size("256x256")
                .user("testing")
                .n(2)
                .build();

        when(mockApi.createImageEdit(any())).thenReturn(just(ImageResult.builder()
            .data(List.of(
                Image.builder().url("https://openai.com/penguin.png").build(),
                Image.builder().url("https://openai.com/penguin2.png").build()
            ))
            .build()));
        List<Image> images = service.createImageEdit(createImageRequest, fileWithAlphaPath, null).getData();
        verify(mockApi, times(1)).createImageEdit(any());
        assertEquals(2, images.size());
        assertNotNull(images.get(0).getUrl());
    }

    @Test
    void createImageEditWithMask() {
        CreateImageEditRequest createImageRequest = CreateImageEditRequest.builder()
                .prompt("a penguin with a red hat")
                .responseFormat("url")
                .size("256x256")
                .user("testing")
                .n(2)
                .build();

        when(mockApi.createImageEdit(any())).thenReturn(just(ImageResult.builder()
            .data(List.of(
                Image.builder().url("https://openai.com/penguin.png").build(),
                Image.builder().url("https://openai.com/penguin2.png").build()))
            .build()));
        List<Image> images = service.createImageEdit(createImageRequest, filePath, maskPath).getData();
        verify(mockApi, times(1)).createImageEdit(any());
        assertEquals(2, images.size());
        assertNotNull(images.get(0).getUrl());
    }

    @Test
    void createImageVariation() {
        CreateImageVariationRequest createImageVariationRequest = CreateImageVariationRequest.builder()
                .responseFormat("url")
                .size("256x256")
                .user("testing")
                .n(2)
                .build();

        when(mockApi.createImageVariation(any())).thenReturn(just(ImageResult.builder()
            .data(List.of(Image.builder().url("https://openai.com/penguin.png").build(),
                Image.builder().url("https://openai.com/penguin2.png").build()))
            .build()));
        List<Image> images = service.createImageVariation(createImageVariationRequest, filePath).getData();
        verify(mockApi, times(1)).createImageVariation(any());
        assertEquals(2, images.size());
        assertNotNull(images.get(0).getUrl());
    }
}
