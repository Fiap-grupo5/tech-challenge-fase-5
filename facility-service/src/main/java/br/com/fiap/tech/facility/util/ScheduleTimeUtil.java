package br.com.fiap.tech.facility.util;

import br.com.fiap.tech.facility.domain.DoctorSchedule;

import java.time.LocalTime;

public class ScheduleTimeUtil {

    /**
     * Verifica se há sobreposição entre os horários de duas agendas.
     * 
     * @param startTime1 horário de início do primeiro período da primeira agenda
     * @param endTime1 horário de término do primeiro período da primeira agenda
     * @param secondPeriodStart1 horário de início do segundo período da primeira agenda
     * @param secondPeriodEnd1 horário de término do segundo período da primeira agenda
     * @param startTime2 horário de início do primeiro período da segunda agenda
     * @param endTime2 horário de término do primeiro período da segunda agenda
     * @param secondPeriodStart2 horário de início do segundo período da segunda agenda
     * @param secondPeriodEnd2 horário de término do segundo período da segunda agenda
     * @return true se houver sobreposição, false caso contrário
     */
    public static boolean hasTimeOverlap(
            LocalTime startTime1, LocalTime endTime1, 
            LocalTime secondPeriodStart1, LocalTime secondPeriodEnd1,
            LocalTime startTime2, LocalTime endTime2, 
            LocalTime secondPeriodStart2, LocalTime secondPeriodEnd2) {
        
        // Verifica sobreposição do primeiro período da agenda 1 com o primeiro período da agenda 2
        if (hasOverlap(startTime1, endTime1, startTime2, endTime2)) {
            return true;
        }
        
        // Se a agenda 2 tiver segundo período, verifica sobreposição do primeiro período da agenda 1 com o segundo período da agenda 2
        if (secondPeriodStart2 != null && secondPeriodEnd2 != null) {
            if (hasOverlap(startTime1, endTime1, secondPeriodStart2, secondPeriodEnd2)) {
                return true;
            }
        }
        
        // Se a agenda 1 tiver segundo período, verifica sobreposição do segundo período da agenda 1 com o primeiro período da agenda 2
        if (secondPeriodStart1 != null && secondPeriodEnd1 != null) {
            if (hasOverlap(secondPeriodStart1, secondPeriodEnd1, startTime2, endTime2)) {
                return true;
            }
            
            // Se ambas as agendas tiverem segundo período, verifica sobreposição dos segundos períodos
            if (secondPeriodStart2 != null && secondPeriodEnd2 != null) {
                if (hasOverlap(secondPeriodStart1, secondPeriodEnd1, secondPeriodStart2, secondPeriodEnd2)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Verifica se há sobreposição entre dois períodos de tempo.
     * 
     * @param start1 horário de início do primeiro período
     * @param end1 horário de término do primeiro período
     * @param start2 horário de início do segundo período
     * @param end2 horário de término do segundo período
     * @return true se houver sobreposição, false caso contrário
     */
    private static boolean hasOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !end1.isBefore(start2) && !end2.isBefore(start1);
    }
    
    /**
     * Verifica se há sobreposição entre uma nova agenda e uma agenda existente.
     * 
     * @param newSchedule nova agenda
     * @param existingSchedule agenda existente
     * @return true se houver sobreposição, false caso contrário
     */
    public static boolean hasScheduleOverlap(DoctorSchedule newSchedule, DoctorSchedule existingSchedule) {
        return hasTimeOverlap(
                newSchedule.getStartTime(), newSchedule.getEndTime(),
                newSchedule.getSecondPeriodStart(), newSchedule.getSecondPeriodEnd(),
                existingSchedule.getStartTime(), existingSchedule.getEndTime(),
                existingSchedule.getSecondPeriodStart(), existingSchedule.getSecondPeriodEnd()
        );
    }
} 