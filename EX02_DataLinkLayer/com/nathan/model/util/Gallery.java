package com.nathan.model.util;

import javafx.scene.image.Image;
/****************************************************************
 * Autor: Nathan Ferraz da Silva
 * Matricula: 201911925
 * Inicio: 23/03/2021
 * Ultima alteracao: 27/03/2021
 * Nome: Gallery
 * Funcao: Lista e instanica todas as imagens para serem utilizadas
 * ***************************************************************/
public abstract class Gallery {
  private static final String path = "/com/nathan/view/img";

  public static final Image INDICADOR = new Image(Gallery.class.getResourceAsStream(path+"/angulo-circulo-direita.png"));
}
