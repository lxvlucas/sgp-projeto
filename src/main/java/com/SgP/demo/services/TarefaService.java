package com.SgP.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SgP.demo.models.Projeto;
import com.SgP.demo.models.Tarefa;
import com.SgP.demo.repositories.ProjetoRepository;
import com.SgP.demo.repositories.TarefaRepository;

@Service
public class TarefaService {
	@Autowired
	private TarefaRepository tarefaRepository;
	
	@Autowired
	private ProjetoRepository projetoRepository;
	
	public void registrarExecucao(Long tarefaId) {
		Tarefa tarefa = tarefaRepository.findById(tarefaId).orElseThrow();
		tarefa.setStatus("Em Validação");
		tarefa.setProgresso(100.0);
		tarefaRepository.save(tarefa);
	}
	
	@Transactional
	public void validarTarefa(Long tarefaId) {
		Tarefa tarefa = tarefaRepository.findById(tarefaId).orElseThrow();
		tarefa.setStatus("Validada");
		tarefaRepository.save(tarefa);
		
		recalcularProgressoProjeto(tarefa.getProjeto().getId());
	}
	
	public void recalcularProgressoProjeto(Long projetoId) {
		Projeto projeto = projetoRepository.findById(projetoId).orElseThrow();
		
		double progressoTotal = 0.0;
		
		for (Tarefa t : projeto.getTarefas()) {
			if ("Validada".equals(t.getStatus()) && t.getPesoPercentual() != null) {
				progressoTotal += t.getPesoPercentual();
			}
		}
		
		projeto.setPercentualConclusao(progressoTotal);
		projetoRepository.save(projeto);
	}
}
