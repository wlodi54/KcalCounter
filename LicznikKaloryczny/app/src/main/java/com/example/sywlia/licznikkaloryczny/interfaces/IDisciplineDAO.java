package com.example.sywlia.licznikkaloryczny.interfaces;

import com.example.sywlia.licznikkaloryczny.models.DisciplineDTO;
import com.example.sywlia.licznikkaloryczny.models.DisciplineSubDTO;

import java.util.ArrayList;

/**
 * Created by sywlia on 2017-05-16.
 */

public interface IDisciplineDAO {
    ArrayList<DisciplineDTO> getDisciplineList();

    void insert(DisciplineDTO discipline);

    ArrayList<DisciplineSubDTO> getArraySubDiscipline(long idDiscipline);

    DisciplineDTO getDiscipline(long idDiscipline);
    DisciplineDTO getDiscipline(String nameDiscypline);

}
