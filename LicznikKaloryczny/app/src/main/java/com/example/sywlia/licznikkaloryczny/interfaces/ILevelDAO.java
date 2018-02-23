package com.example.sywlia.licznikkaloryczny.interfaces;

import com.example.sywlia.licznikkaloryczny.models.LevelDTO;

/**
 * Created by sywlia on 2017-05-16.
 */

public interface ILevelDAO {
    LevelDTO getPoziom(int iloscMedali);
    int getImgSrc(long id);
}
