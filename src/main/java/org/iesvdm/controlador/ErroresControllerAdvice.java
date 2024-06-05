package org.iesvdm.controlador;

import org.iesvdm.controlador.Excepciones.MiExcepcion;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErroresControllerAdvice {

    //AQUI VAMOS A CREAR UNA EXCEPCION PERSONALIZADA, USANDO LA CLASE MiExcepcion CREADA
    @ExceptionHandler(MiExcepcion.class)
    public String handleMiException(MiExcepcion miExcepcion, Model model) {
        model.addAttribute("traza", miExcepcion.getMessage());

        return "error-mi-excepcion";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleAllUncaughtException(RuntimeException exception, Model model) {
        model.addAttribute("traza", exception);

        return "error";
    }
}