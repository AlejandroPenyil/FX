package com.example.fxtry.iService;

import com.example.fxtry.Model.ConversacionDTO;
import com.example.fxtry.Model.MensajeDTO;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface iMensajeriaService {
    @GET("mensajeria/conversaciones")
    Call<List<ConversacionDTO>> getAllConversaciones();

    @GET("mensajeria/conversaciones/{id}")
    Call<ConversacionDTO> getConversacionById(@Path("id") Integer id);

    @GET("mensajeria/usuarios/{usuarioId}/conversacion")
    Call<ConversacionDTO> getConversacionByUsuario(@Path("usuarioId") Integer usuarioId);

    @POST("mensajeria/mensajes")
    Call<MensajeDTO> enviarMensaje(@Body MensajeDTO mensajeDTO);

    @POST("mensajeria/conversaciones/{conversacionId}/usuarios/{usuarioId}/leidos")
    Call<Void> marcarComoLeidos(@Path("conversacionId") Integer conversacionId, @Path("usuarioId") Integer usuarioId);
}