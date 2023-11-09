package com.theokanning.openai.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.TextNode;
import com.theokanning.openai.DeleteResult;
import com.theokanning.openai.OpenAiError;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.CreateTranslationRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.audio.TranslationResult;
import com.theokanning.openai.billing.BillingUsage;
import com.theokanning.openai.billing.Subscription;
import com.theokanning.openai.client.OpenAiApi;
import com.theokanning.openai.completion.CompletionChunk;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.edit.EditRequest;
import com.theokanning.openai.edit.EditResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import com.theokanning.openai.file.File;
import com.theokanning.openai.fine_tuning.FineTuningEvent;
import com.theokanning.openai.fine_tuning.FineTuningJob;
import com.theokanning.openai.fine_tuning.FineTuningJobRequest;
import com.theokanning.openai.finetune.FineTuneEvent;
import com.theokanning.openai.finetune.FineTuneRequest;
import com.theokanning.openai.finetune.FineTuneResult;
import com.theokanning.openai.image.CreateImageEditRequest;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.CreateImageVariationRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.model.Model;
import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.moderation.ModerationResult;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class OpenAiService {

    private static final String BASE_URL = "https://api.openai.com/";
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final ObjectMapper mapper = defaultObjectMapper();

    private final OpenAiApi api;
    private final ExecutorService executorService;

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     */
    public OpenAiService(final String token) {
        this(token, DEFAULT_TIMEOUT);
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi
     *
     * @param token   OpenAi token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     * @param timeout http read timeout, Duration.ZERO means no timeout
     */
    public OpenAiService(final String token, final Duration timeout) {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(token, timeout);
        Retrofit retrofit = defaultRetrofit(client, mapper);

        this.api = retrofit.create(OpenAiApi.class);
        this.executorService = client.dispatcher().executorService();
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi.
     * Use this if you need more customization, but use OpenAiService(api, executorService) if you use streaming and
     * want to shut down instantly
     *
     * @param api OpenAiApi instance to use for all methods
     */
    public OpenAiService(final OpenAiApi api) {
        this.api = api;
        this.executorService = null;
    }

    /**
     * Creates a new OpenAiService that wraps OpenAiApi.
     * The ExecutorService must be the one you get from the client you created the api with
     * otherwise shutdownExecutor() won't work.
     * <p>
     * Use this if you need more customization.
     *
     * @param api             OpenAiApi instance to use for all methods
     * @param executorService the ExecutorService from client.dispatcher().executorService()
     */
    public OpenAiService(final OpenAiApi api, final ExecutorService executorService) {
        this.api = api;
        this.executorService = executorService;
    }

    public List<Model> listModels() {
        return execute(api.listModels()).data;
    }

    public Model getModel(String modelId) {
        return execute(api.getModel(modelId));
    }

    /**
     * Creates a text completion request using the OpenAI API.
     * This is typically used for generating text based on a provided prompt.
     *
     * @param request The completion request object, containing parameters like the prompt and settings.
     * @return CompletionResult The result object with the generated text and other details.
     */
    public CompletionResult createCompletion(CompletionRequest request) {
        // Executes the API call to create a completion and returns the response.
        return execute(api.createCompletion(request));
    }

    public Flowable<CompletionChunk> streamCompletion(CompletionRequest request) {
        request.setStream(true);

        return stream(api.createCompletionStream(request), CompletionChunk.class);
    }

    /**
     * Initiates a chat request using the OpenAI API.
     * This method is used for interactive chat-like conversations, where the API generates responses.
     *
     * @param request The chat request object, containing the initial message or conversation context.
     * @return ChatCompletionResult The result object with the chat response.
     */
    public ChatCompletionResult createChatCompletion(ChatCompletionRequest request) {
        // Executes the API call for a chat completion and returns the response.
        return execute(api.createChatCompletion(request));
    }

    public Flowable<ChatCompletionChunk> streamChatCompletion(ChatCompletionRequest request) {
        request.setStream(true);

        return stream(api.createChatCompletionStream(request), ChatCompletionChunk.class);
    }

    /**
     * Creates an edit request using the OpenAI API.
     * This method is typically used for making small edits to a provided piece of text.
     *
     * @param request The edit request object, containing the original text and the desired edit.
     * @return EditResult The result object with the edited text.
     */
    public EditResult createEdit(EditRequest request) {
        // Executes the API call to create an edit and returns the response.
        return execute(api.createEdit(request));
    }

    public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
        return execute(api.createEmbeddings(request));
    }

    /**
     * Retrieves a list of files that have been uploaded to the OpenAI API.
     * This method is useful for managing and referencing previously uploaded files.
     *
     * @return List<File> A list of File objects representing the uploaded files.
     */
    public List<File> listFiles() {
        // Executes the API call to retrieve a list of uploaded files and returns the data.
        return execute(api.listFiles()).data;
    }

    public File uploadFile(String purpose, String filepath) {
        java.io.File file = new java.io.File(filepath);
        RequestBody purposeBody = RequestBody.create(okhttp3.MultipartBody.FORM, purpose);
        RequestBody fileBody = RequestBody.create(MediaType.parse("text"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", filepath, fileBody);

        return execute(api.uploadFile(purposeBody, body));
    }

    /**
     * Deletes a file that has been uploaded to the OpenAI API.
     * This method is used to remove files that are no longer needed.
     *
     * @param fileId The ID of the file to be deleted.
     * @return DeleteResult The result of the deletion operation.
     */
    public DeleteResult deleteFile(String fileId) {
        // Executes the API call to delete the specified file and returns the response.
        return execute(api.deleteFile(fileId));
    }


    public File retrieveFile(String fileId) {
        return execute(api.retrieveFile(fileId));
    }

    public ResponseBody retrieveFileContent(String fileId) {
        return execute(api.retrieveFileContent(fileId));
    }

    public FineTuningJob createFineTuningJob(FineTuningJobRequest request) {
        return execute(api.createFineTuningJob(request));
    }

    public List<FineTuningJob> listFineTuningJobs() {
        return execute(api.listFineTuningJobs()).data;
    }

    
    public FineTuningJob retrieveFineTuningJob(String fineTuningJobId) {
        return execute(api.retrieveFineTuningJob(fineTuningJobId));
    }

    public FineTuningJob cancelFineTuningJob(String fineTuningJobId) {
        return execute(api.cancelFineTuningJob(fineTuningJobId));
    }

    public List<FineTuningEvent> listFineTuningJobEvents(String fineTuningJobId) {
        return execute(api.listFineTuningJobEvents(fineTuningJobId)).data;
    }

    @Deprecated
    public FineTuneResult createFineTune(FineTuneRequest request) {
        return execute(api.createFineTune(request));
    }

    public CompletionResult createFineTuneCompletion(CompletionRequest request) {
        return execute(api.createFineTuneCompletion(request));
    }

    @Deprecated
    public List<FineTuneResult> listFineTunes() {
        return execute(api.listFineTunes()).data;
    }

    @Deprecated
    public FineTuneResult retrieveFineTune(String fineTuneId) {
        return execute(api.retrieveFineTune(fineTuneId));
    }

    @Deprecated
    public FineTuneResult cancelFineTune(String fineTuneId) {
        return execute(api.cancelFineTune(fineTuneId));
    }

    @Deprecated
    public List<FineTuneEvent> listFineTuneEvents(String fineTuneId) {
        return execute(api.listFineTuneEvents(fineTuneId)).data;
    }

    public DeleteResult deleteFineTune(String fineTuneId) {
        return execute(api.deleteFineTune(fineTuneId));
    }

    /**
     * Initiates an image generation request using the OpenAI API.
     * This method is used for creating images based on textual descriptions.
     *
     * @param request The image creation request object, containing the description and image parameters.
     * @return ImageResult The result object with the generated image.
     */
    public ImageResult createImage(CreateImageRequest request) {
        // Executes the API call to generate an image and returns the response.
        return execute(api.createImage(request));
    }

    /**
     * Submits an image editing request to the OpenAI API.
     * This method is used for making edits to existing images based on textual instructions.
     *
     * @param request The image edit request object, containing the original image and edit instructions.
     * @param imagePath The path to the original image file to be edited.
     * @param maskPath The path to the mask file, if applicable.
     * @return ImageResult The result object with the edited image.
     */
    public ImageResult createImageEdit(CreateImageEditRequest request, String imagePath, String maskPath) {
        // Converts the image file path to a File object.
        java.io.File image = new java.io.File(imagePath);
        // Initializes a File object for the mask, if a mask path is provided.
        java.io.File mask = null;
        if (maskPath != null) {
            mask = new java.io.File(maskPath);
        }
        // Calls the overloaded createImageEdit method with the File objects.
        return createImageEdit(request, image, mask);
    }

    public ImageResult createImageEdit(CreateImageEditRequest request, java.io.File image, java.io.File mask) {
        RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("prompt", request.getPrompt())
                .addFormDataPart("size", request.getSize())
                .addFormDataPart("response_format", request.getResponseFormat())
                .addFormDataPart("image", "image", imageBody);

        if (request.getN() != null) {
            builder.addFormDataPart("n", request.getN().toString());
        }

        if (mask != null) {
            RequestBody maskBody = RequestBody.create(MediaType.parse("image"), mask);
            builder.addFormDataPart("mask", "mask", maskBody);
        }

        return execute(api.createImageEdit(builder.build()));
    }

    /**
     * Initiates a request to the OpenAI API to create variations of a given image.
     * This method is used for generating different versions of an image based on certain parameters.
     *
     * @param request The image variation request object, containing the original image and variation parameters.
     * @param imagePath The path to the original image file to generate variations from.
     * @return ImageResult The result object with the generated image variations.
     */
    public ImageResult createImageVariation(CreateImageVariationRequest request, String imagePath) {
        // Converts the image file path to a File object.
        java.io.File image = new java.io.File(imagePath);
        // Calls the overloaded createImageVariation method with the File object.
        return createImageVariation(request, image);
    }

    /**
     * Initiates a request to the OpenAI API to create variations of a given image.
     * This method is used for generating different versions of an image based on specified parameters.
     *
     * @param request The image variation request object, containing parameters for generating image variations
     *                such as size, response format, and number of variations (n).
     * @param image The image file to be used as the basis for generating variations.
     * @return ImageResult The result object containing the generated image variations.
     */
    public ImageResult createImageVariation(CreateImageVariationRequest request, java.io.File image) {
        RequestBody imageBody = RequestBody.create(MediaType.parse("image"), image);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("size", request.getSize())
                .addFormDataPart("response_format", request.getResponseFormat())
                .addFormDataPart("image", "image", imageBody);

        if (request.getN() != null) {
            builder.addFormDataPart("n", request.getN().toString());
        }

        return execute(api.createImageVariation(builder.build()));
    }

    /**
     * Submits a transcription request to the OpenAI API.
     * This method is typically used for converting audio content into text.
     *
     * @param request The transcription request object, containing details about the audio to transcribe.
     * @param audioPath The path to the audio file to be transcribed.
     * @return TranscriptionResult The result object with the transcribed text.
     */
    public TranscriptionResult createTranscription(CreateTranscriptionRequest request, String audioPath) {
        // Converts the audio file path to a File object.
        java.io.File audio = new java.io.File(audioPath);
        // Calls the overloaded createTranscription method with the File object.
        return createTranscription(request, audio);
    }

    /**
     * Submits a transcription request to the OpenAI API.
     * This method is typically used for converting audio content into text.
     *
     * @param request The transcription request object, containing parameters such as the model to use,
     *                optional prompt, response format, temperature, and language for the transcription.
     * @param audio The path to the audio file to be transcribed.
     * @return TranscriptionResult The result object containing the transcribed text and potentially other
     *                             response details.
     */
    public TranscriptionResult createTranscription(CreateTranscriptionRequest request, java.io.File audio) {
        RequestBody audioBody = RequestBody.create(MediaType.parse("audio"), audio);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("model", request.getModel())
                .addFormDataPart("file", audio.getName(), audioBody);

        if (request.getPrompt() != null) {
            builder.addFormDataPart("prompt", request.getPrompt());
        }
        if (request.getResponseFormat() != null) {
            builder.addFormDataPart("response_format", request.getResponseFormat());
        }
        if (request.getTemperature() != null) {
            builder.addFormDataPart("temperature", request.getTemperature().toString());
        }
        if (request.getLanguage() != null) {
            builder.addFormDataPart("language", request.getLanguage());
        }

        return execute(api.createTranscription(builder.build()));
    }

    /**
     * Submits a translation request to the OpenAI API.
     * This method is used for translating text from one language to another.
     *
     * @param request The translation request object, containing the text to translate and target language.
     * @param audioPath The path to the audio file for translation.
     * @return TranslationResult The result object with the translated text.
     */
    public TranslationResult createTranslation(CreateTranslationRequest request, String audioPath) {
        // Converts the audio file path to a File object.
        java.io.File audio = new java.io.File(audioPath);
        // Calls the overloaded createTranslation method with the File object.
        return createTranslation(request, audio);
    }

    /**
     * Submits a translation request to the OpenAI API.
     * This method is used for translating the content of an audio file from one language to another.
     *
     * @param request The translation request object, containing parameters such as the model to use,
     *                optional prompt, response format, temperature, etc., for the translation process.
     * @param audio The audio file to be translated.
     * @return TranslationResult The result object containing the translated text.
     */
    public TranslationResult createTranslation(CreateTranslationRequest request, java.io.File audio) {
        RequestBody audioBody = RequestBody.create(MediaType.parse("audio"), audio);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MediaType.get("multipart/form-data"))
                .addFormDataPart("model", request.getModel())
                .addFormDataPart("file", audio.getName(), audioBody);

        if (request.getPrompt() != null) {
            builder.addFormDataPart("prompt", request.getPrompt());
        }
        if (request.getResponseFormat() != null) {
            builder.addFormDataPart("response_format", request.getResponseFormat());
        }
        if (request.getTemperature() != null) {
            builder.addFormDataPart("temperature", request.getTemperature().toString());
        }

        return execute(api.createTranslation(builder.build()));
    }

    public ModerationResult createModeration(ModerationRequest request) {
        return execute(api.createModeration(request));
    }

    /**
     * Calls the Open AI api, returns the response, and parses error messages if the request fails
     */
    public static <T> T execute(Single<T> apiCall) {
        try {
            return apiCall.blockingGet();
        } catch (HttpException e) {
            try {
                if (e.response() == null || e.response().errorBody() == null) {
                    throw e;
                }
                String errorBody = e.response().errorBody().string();

                OpenAiError error = mapper.readValue(errorBody, OpenAiError.class);
                throw new OpenAiHttpException(error, e, e.code());
            } catch (IOException ex) {
                // couldn't parse OpenAI error
                throw e;
            }
        }
    }

    /**
     * Calls the Open AI api and returns a Flowable of SSE for streaming
     * omitting the last message.
     *
     * @param apiCall The api call
     */
    public static Flowable<SSE> stream(Call<ResponseBody> apiCall) {
        return stream(apiCall, false);
    }

    /**
     * Calls the Open AI api and returns a Flowable of SSE for streaming.
     *
     * @param apiCall  The api call
     * @param emitDone If true the last message ([DONE]) is emitted
     */
    public static Flowable<SSE> stream(Call<ResponseBody> apiCall, boolean emitDone) {
        return Flowable.create(emitter -> apiCall.enqueue(new ResponseBodyCallback(emitter, emitDone)), BackpressureStrategy.BUFFER);
    }

    /**
     * Calls the Open AI api and returns a Flowable of type T for streaming
     * omitting the last message.
     *
     * @param apiCall The api call
     * @param cl      Class of type T to return
     */
    public static <T> Flowable<T> stream(Call<ResponseBody> apiCall, Class<T> cl) {
        return stream(apiCall).map(sse -> mapper.readValue(sse.getData(), cl));
    }

    /**
     * Shuts down the OkHttp ExecutorService.
     * The default behaviour of OkHttp's ExecutorService (ConnectionPool)
     * is to shut down after an idle timeout of 60s.
     * Call this method to shut down the ExecutorService immediately.
     */
    public void shutdownExecutor() {
        Objects.requireNonNull(this.executorService, "executorService must be set in order to shut down");
        this.executorService.shutdown();
    }

    public static OpenAiApi buildApi(String token, Duration timeout) {
        ObjectMapper mapper = defaultObjectMapper();
        OkHttpClient client = defaultClient(token, timeout);
        Retrofit retrofit = defaultRetrofit(client, mapper);

        return retrofit.create(OpenAiApi.class);
    }

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.addMixIn(ChatFunction.class, ChatFunctionMixIn.class);
        mapper.addMixIn(ChatCompletionRequest.class, ChatCompletionRequestMixIn.class);
        mapper.addMixIn(ChatFunctionCall.class, ChatFunctionCallMixIn.class);
        return mapper;
    }

    public static OkHttpClient defaultClient(String token, Duration timeout) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(token))
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .build();
    }

    public static Retrofit defaultRetrofit(OkHttpClient client, ObjectMapper mapper) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * Transforms a stream of chat completion chunks into a stream of chat message accumulators.
     * Each chat message accumulator contains the latest chat message chunk and an accumulated chat message.
     *
     * @param flowable A Flowable stream of ChatCompletionChunk objects. Each ChatCompletionChunk
     *                 represents a part of the chat message response.
     * @return A Flowable stream of ChatMessageAccumulator objects. Each accumulator combines
     *         information about the current chat message chunk and the overall accumulated message.
     */
    public Flowable<ChatMessageAccumulator> mapStreamToAccumulator(Flowable<ChatCompletionChunk> flowable) {
        // Initialize an empty ChatFunctionCall and an empty ChatMessage for accumulation.
        ChatFunctionCall functionCall = new ChatFunctionCall(null, null);
        ChatMessage accumulatedMessage = new ChatMessage(ChatMessageRole.ASSISTANT.value(), null);

        // Process each chunk in the flowable stream.
        return flowable.map(chunk -> {
            // Extract the first chat message from the chunk.
            ChatMessage messageChunk = chunk.getChoices().get(0).getMessage();

            // If the message chunk contains a function call, accumulate its name and arguments.
            if (messageChunk.getFunctionCall() != null) {
                // Accumulate function call name.
                if (messageChunk.getFunctionCall().getName() != null) {
                    String namePart = messageChunk.getFunctionCall().getName();
                    functionCall.setName((functionCall.getName() == null ? "" : functionCall.getName()) + namePart);
                }
                // Accumulate function call arguments.
                if (messageChunk.getFunctionCall().getArguments() != null) {
                    String argumentsPart = messageChunk.getFunctionCall().getArguments() == null ? "" : messageChunk.getFunctionCall().getArguments().asText();
                    functionCall.setArguments(new TextNode((functionCall.getArguments() == null ? "" : functionCall.getArguments().asText()) + argumentsPart));
                }
                // Update the accumulated message with the accumulated function call.
                accumulatedMessage.setFunctionCall(functionCall);
            } else {
                // If there's no function call, accumulate the message content.
                accumulatedMessage.setContent((accumulatedMessage.getContent() == null ? "" : accumulatedMessage.getContent()) + (messageChunk.getContent() == null ? "" : messageChunk.getContent()));
            }

            // Check if this is the last chunk and update the function call arguments as necessary.
            if (chunk.getChoices().get(0).getFinishReason() != null) { // last
                if (functionCall.getArguments() != null) {
                    functionCall.setArguments(mapper.readTree(functionCall.getArguments().asText()));
                    accumulatedMessage.setFunctionCall(functionCall);
                }
            }

            // Return a new ChatMessageAccumulator containing the current message chunk and the accumulated message.
            return new ChatMessageAccumulator(messageChunk, accumulatedMessage);
        });
    }

    /**
     * Account information inquiry: including total amount and other information.
     *
     * @return Account information.
     */
    public Subscription subscription() {
        Single<Subscription> subscription = api.subscription();
        return subscription.blockingGet();
    }

    /**
     * Account API consumption amount information inquiry.
     * Up to 100 days of inquiry.
     *
     * @param starDate
     * @param endDate
     * @return Consumption amount information.
     */
    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        Single<BillingUsage> billingUsage = api.billingUsage(starDate, endDate);
        return billingUsage.blockingGet();
    }

}
