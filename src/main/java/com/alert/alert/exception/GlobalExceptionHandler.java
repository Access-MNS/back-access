//package com.alert.alert.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(ChannelNotFoundException.class)
//    public ResponseEntity<String> handleChannelNotFoundException(ChannelNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(MessageSaveException.class)
//    public ResponseEntity<String> handleMessageSaveException(MessageSaveException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(ChannelAlreadyExistsException.class)
//    public ResponseEntity<String> handleChannelAlreadyExistsException(ChannelAlreadyExistsException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(ParentChannelNotFoundException.class)
//    public ResponseEntity<String> handleParentChannelNotFoundException(ParentChannelNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(MessageAlreadyExistsException.class)
//    public ResponseEntity<String> handleMessageAlreadyExistsException(MessageAlreadyExistsException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(ChannelSaveException.class)
//    public ResponseEntity<String> handleChannelSaveException(ChannelSaveException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//    }
//}