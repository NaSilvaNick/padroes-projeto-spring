package dio.gof.service.impl;

import dio.gof.model.Cliente;
import dio.gof.model.Endereco;
import dio.gof.repository.ClienteRepository;
import dio.gof.repository.EnderecoRepository;
import dio.gof.service.ClienteService;
import dio.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

  @Autowired
  private ClienteRepository clienteRepository;
  @Autowired
  private EnderecoRepository enderecoRepository;
  @Autowired
  private ViaCepService viaCepService;

  @Override
  public Iterable<Cliente> buscarTodos() {
    return clienteRepository.findAll();
  }

  @Override
  public Cliente buscarPorId(Long id) {
    Optional<Cliente> clienteOp = clienteRepository.findById(id);
    return clienteOp.get();
  }

  private void salvarClienteComCep(Cliente cliente) {
    String cep = cliente.getEndereco().getCep();
    Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
      Endereco novoEndereco = viaCepService.consultarCep(cep);
      enderecoRepository.save(novoEndereco);
      return novoEndereco;
    });
    cliente.setEndereco(endereco);
    clienteRepository.save(cliente);
  }

  @Override
  public void inserir(Cliente cliente) {
    salvarClienteComCep(cliente);
  }

  @Override
  public void atualizar(Long id, Cliente cliente) {
    Optional<Cliente> clienteOp = clienteRepository.findById(id);
    if (clienteOp.isPresent()) {
      salvarClienteComCep(cliente);
    }
  }

  @Override
  public void deletar(Long id) {
    Optional<Cliente> clienteOp = clienteRepository.findById(id);
    if (clienteOp.isPresent()) {
      clienteRepository.deleteById(id);
    }
  }
}
