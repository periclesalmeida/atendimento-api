package br.com.periclesalmeida.atendimento.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DataUtils {

	private static Date dataAtual;
	
	public static Date getDataAtual() {
		return VerificadorUtil.estaNulo(dataAtual) ? Calendar.getInstance(TimeZone.getTimeZone("GMT-03:00")).getTime() : dataAtual;
	}
	
	public static void setDataAtual(Date data) {
		dataAtual = data;
	}
	
	public static boolean dataAtualEstaEntrePeriodos(Date dataInicial, Date dataFinal) {
		return getDataAtual().after(dataInicial) && getDataAtual().before(dataFinal);
	}
	
	public static boolean dataEstaEntrePeriodos(Date dataVerificada, Date dataInicial, Date dataFinal) {
		return dataVerificada.after(dataInicial) && dataVerificada.before(dataFinal);
	}
	
	public static boolean datasIguais(Date datePrimeira, Date dataSegunda) {
		String primeiraData = converterDataParaString(datePrimeira);
		String segundaData = converterDataParaString(dataSegunda);
		return primeiraData.equals(segundaData);
	}
	
	public static Date getPrimeiroDiaDoMesAtual() {
		return getPrimeiroDiaDoMes(Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
	}
	
	public static Date getUltimoDiaDoMesAtual() {
		return getUltimoDiaDoMes(Calendar.getInstance().get(Calendar.MONTH)+1, Calendar.getInstance().get(Calendar.YEAR));
	}
	
	public static Date getUltimoDiaDoMes(Date mes) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(mes);
		return DataUtils.obterData(calendar.getActualMaximum(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
	}
	
	public static Date getPrimeiroDiaDoMes(Integer mes, Integer ano) {
		Calendar calendario = getCalendar();
		calendario.set(Calendar.MONTH, mes-1);
		calendario.set(Calendar.YEAR, ano);
		calendario.set(Calendar.DAY_OF_MONTH, 1);
		calendario.set(Calendar.HOUR_OF_DAY, 0);
		calendario.set(Calendar.MINUTE, 0);
		calendario.set(Calendar.SECOND, 0);
		return calendario.getTime();
	}
	
	public static Date getUltimoDiaDoMes(Integer mes, Integer ano) {
		Calendar calendario = getCalendar();
		calendario.set(Calendar.MONTH, mes-1);
		calendario.set(Calendar.YEAR, ano);
		calendario.set(Calendar.DAY_OF_MONTH, calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendario.set(Calendar.HOUR_OF_DAY, 23);
		calendario.set(Calendar.MINUTE, 59);
		calendario.set(Calendar.SECOND, 59);
		return calendario.getTime();
	}
	
	public static Date getPrimeiroDiaDoMesAnterior() {
		Calendar calendarMesAnterior = obterCalendario(acrescentarMeses(getDataAtual(), -1));
		return DataUtils.obterData(1, calendarMesAnterior.get(Calendar.MONTH), calendarMesAnterior.get(Calendar.YEAR));
	}
	
	public static Date getUltimoDiaDoMesAnterior() {
		Calendar calendarMesAnterior = obterCalendario(acrescentarMeses(getDataAtual(), -1));
		return DataUtils.obterData(calendarMesAnterior.getActualMaximum(Calendar.DAY_OF_MONTH), calendarMesAnterior.get(Calendar.MONTH), calendarMesAnterior.get(Calendar.YEAR));
	}
	
	public static int getQuantidadeDeSemanasNoAno(int ano) {
		Calendar calendar = getCalendar();
		calendar.set(ano, Calendar.DECEMBER, 31);
		int total = calendar.get(Calendar.WEEK_OF_YEAR);
		if (total == 1) {
			calendar.set(ano, Calendar.DECEMBER, 24);
			total = calendar.get(Calendar.WEEK_OF_YEAR);
		}
		return total;
	}

	private static Calendar getCalendar() {
		GregorianCalendar calendario = new GregorianCalendar();
		calendario.setFirstDayOfWeek(Calendar.SUNDAY);
		calendario.setMinimalDaysInFirstWeek(4);
		return calendario;
	}
	
	public static Calendar obterCalendario(int ano, int numeroSemana, int diaDaSemana) {
		Calendar calendario = getCalendar();
		calendario.set(Calendar.YEAR, ano);
		calendario.set(Calendar.WEEK_OF_YEAR, numeroSemana);
		calendario.set(Calendar.DAY_OF_WEEK, diaDaSemana);
		return calendario; 
	}
	
	public static Date obterData(int dia, int mes, int ano) {
		Calendar calendario = getCalendar();
		calendario.set(Calendar.DAY_OF_MONTH, dia);
		calendario.set(Calendar.MONTH, mes);
		calendario.set(Calendar.YEAR, ano);
		return calendario.getTime(); 
	}
	
	public static Calendar obterCalendario(Date data) {
		if (VerificadorUtil.estaNulo(data)) {
			return null;
		}
		Calendar calendario = getCalendar();
		calendario.setTime(data);
		return calendario; 
	}
	
	public static Date converterStringParaDataNoFormato(String dataString, String formato) {
		try {
			if (VerificadorUtil.naoEstaNuloOuVazio(dataString)) {
				return new SimpleDateFormat(formato).parse(dataString);
			}
			return null;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Não foi possível converter a data informada.");
		}
	}
	
	public static Date converterStringParaData(String dataString) {
		try {
			if (VerificadorUtil.naoEstaNuloOuVazio(dataString)) {
				return new SimpleDateFormat("dd/MM/yyyy").parse(dataString);
			}
			return null;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Não foi possível converter a data informada.");
		}
	}
	
	public static Date converterStringParaDataComHorario(String dataString) {
		try {
			if (VerificadorUtil.naoEstaNulo(dataString)) {
				return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataString);
			}
			return null;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Não foi possível converter a data informada.");
		}
	}
	
	public static String converterDataParaString(Date data) {
		if (VerificadorUtil.naoEstaNulo(data)) {
			return new SimpleDateFormat("dd/MM/yyyy").format(data);
		}
		return null;
	}
	
	public static String converterDataParaStringNoFormato(Date data, String formato) {
		if (VerificadorUtil.naoEstaNulo(data)) {
			return new SimpleDateFormat(formato).format(data);
		}
		return null;
	}
	
	public static String converterDataComHorarioParaString(Date data) {
		if (VerificadorUtil.naoEstaNulo(data)) {
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data);
		}
		return null;
	}
	
	public static Date getDataComHorarioMinimo(Date data) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.set(Calendar.HOUR_OF_DAY, 0);
		calendario.set(Calendar.MINUTE, 0);
		calendario.set(Calendar.SECOND, 0);
		return calendario.getTime();
	}
	
	public static Date getDataComHorarioMaximo(Date data) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.set(Calendar.HOUR_OF_DAY, 23);
		calendario.set(Calendar.MINUTE, 59);
		calendario.set(Calendar.SECOND, 59);
		return calendario.getTime();
	}
	
	public static Date acrescentarUmMinuto(Date data) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.add(Calendar.MINUTE, 1);
		return calendario.getTime();
	}
	
	public static Date acrescentarDiasResultadoDiaUtil(Date data, Integer quantidadeDias) {
		Date diaUtil = acrescentarDias(data, quantidadeDias);
		diaUtil = proximoDiaUtil(diaUtil);
		return diaUtil;
	}
	
	public static Date subtrairDias(Date data, Integer quantidadeDias) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.add(Calendar.DATE, (quantidadeDias * -1));
		return calendario.getTime();
	}
	
	public static Date acrescentarDias(Date data, Integer quantidadeDias) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.add(Calendar.DATE, quantidadeDias);
		return calendario.getTime();
	}
	
	public static Integer getDia(Date data) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		return calendario.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Integer getMes(Date data) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		return calendario.get(Calendar.MONTH);
	}
	
	public static Integer getAno(Date data) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		return calendario.get(Calendar.YEAR);
	}

	public static Date acrescentarMeses(Date data, Integer quantidadeMeses) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.add(Calendar.MONTH, quantidadeMeses);
		return calendario.getTime();
	}

	public static Date acrescentarAnos(Date data, Integer quantidadeAnos) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(data);
		calendario.add(Calendar.YEAR, quantidadeAnos);
		return calendario.getTime();
	}
	
	public static Date proximoDiaUtil(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);  
		int day = cal.get(Calendar.DAY_OF_WEEK);
		switch (day) {  
		    case Calendar.SATURDAY:
		        cal.add(Calendar.DAY_OF_WEEK, 2);
		        break;  
		    case Calendar.SUNDAY:
		        cal.add(Calendar.DAY_OF_WEEK, 1);
		        break;  
		    default:  
		        break;  
		}  
		return cal.getTime();
	}
	
	public static Integer calcularDiferenceEmDiasEntreDuasDatas(Date dataFinal, Date dataInicial) {
		try {
			Calendar calendarDataInicial = new GregorianCalendar();
			calendarDataInicial.setTime(dataInicial);
			Calendar calendarDataFinal = new GregorianCalendar();
			calendarDataFinal.setTime(dataFinal);
			
			Long diferencaEntreAsDatasEmMilisegundos = calendarDataFinal.getTimeInMillis() - calendarDataInicial.getTimeInMillis();
		    Integer milisegundosDeUmDia = 1000 * 60 * 60 * 24;
		    Long quantidadeDeDiasDeDiferenca = diferencaEntreAsDatasEmMilisegundos / milisegundosDeUmDia;
		    return quantidadeDeDiasDeDiferenca.intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Date calcularAhSomaDeDiasAhUmaData(Date data, Integer dias) {
		try {
			Date dataRetornada = data;
			dataRetornada.setTime(dataRetornada.getTime() + TimeUnit.DAYS.toMillis(dias));
			return dataRetornada;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date calcularAhDiferencaDeDiasAhUmaData(Date data, Integer dias) {
		try {
			Date dataRetornada = data;
			dataRetornada.setTime(dataRetornada.getTime() - TimeUnit.DAYS.toMillis(dias));
			return dataRetornada;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date calcularEhSubtrairDeHorasAhUmaData(Date data, Integer horas) {
		try {
			Date dataRetornada = data;
			dataRetornada.setTime(dataRetornada.getTime() - TimeUnit.HOURS.toMillis(horas));
			return dataRetornada;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Date calcularEhSubtrairDeHorasIhMinutosAhUmaData(Date data, Integer horas, Integer minutos) {
		try {
			Date dataRetornada = data;
			dataRetornada.setTime(dataRetornada.getTime() - TimeUnit.HOURS.toMillis(horas) - TimeUnit.MINUTES.toMillis(minutos));
			return dataRetornada;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean verificarSeDataInicialEhMenorQueDataFinalConsiderandoAsHoras(Date dataInicial, Date dataFinal) {
		if(VerificadorUtil.estaNulo(dataInicial)) {
			throw new RuntimeException("Obrigatório informar a data inicial.");
		}
		if(VerificadorUtil.estaNulo(dataFinal)) {
			throw new RuntimeException("Obrigatório informar a data final.");
		}
		return dataInicial.before(dataFinal);
	}
	
	public static String getTextoTempoDecorrido(Date dataInicio, Date dataFim) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dataInicio);
		
		Long tempo = (dataFim.getTime() - calendar.getTimeInMillis()) / 1000;
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(dataInicio);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.add(Calendar.SECOND, tempo.intValue());
		
		StringBuilder tempoDecorrido = new StringBuilder();
		
		if(calendar2.get(Calendar.HOUR_OF_DAY) > 0) {
			String hora = String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY));
			tempoDecorrido.append(hora);
			tempoDecorrido.append(" hora(s)");
		}

		if(VerificadorUtil.naoEstaNuloOuVazio(tempoDecorrido.toString())) {
			tempoDecorrido.append(" e ");
		}
		
		String minuto = String.valueOf(calendar2.get(Calendar.MINUTE));
		tempoDecorrido.append(minuto);
		tempoDecorrido.append(" minuto(s)");
		
		return tempoDecorrido.toString();
	}
	
}