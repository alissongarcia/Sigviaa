/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import modelo.Cidade;
import modelo.Motorista;
import modelo.Solicitacao;
import modelo.Veiculo;
import modelo.Viagem;
import negocio.ManterCidadeNegocio;
import negocio.ManterMotoristaNegocio;
import negocio.ManterVeiculoNegocio;
import negocio.ManterViagemNegocio;
import util.SendMail;

/**
 *
 * @author Alisson
 */
@ManagedBean
@RequestScoped
public class ManterViagemMB {

    private Viagem viagem = new Viagem();
    private Viagem selecionada = new Viagem();
    private Motorista motorista = new Motorista();
    private Veiculo veiculo = new Veiculo();
    private Cidade destino = new Cidade();
    private Cidade origem = new Cidade();
    private String status;
    private String justificativa;
    private Date dataSaida;
    private Date dataRetorno;

    private ManterMotoristaNegocio mmn;
    private ManterCidadeNegocio mcn;
    private ManterVeiculoNegocio mven;
    private ManterViagemNegocio mvn;
    private Converter converter;
    List<Veiculo> l;
    Veiculo veiculo2;

    public ManterViagemMB() {

        mvn = new ManterViagemNegocio();
        mmn = new ManterMotoristaNegocio();
        mven = new ManterVeiculoNegocio();
        mcn = new ManterCidadeNegocio();

        carregarValoresPadrao();
        converter = new Converter() {

            @Override
            public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
                if (string == null || string.equals("")) {
                    return null;
                } else {
                    Key key = KeyFactory.stringToKey(string);
                    return key;
                }
            }

            @Override
            public String getAsString(FacesContext fc, UIComponent uic, Object o) {

                return KeyFactory.keyToString((Key) o);

            }
        };
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    public Viagem getSelecionado() {
        return getSelecionada();
    }

    public void setSelecionado(Viagem selecionada) {
        this.setSelecionada(selecionada);
    }

    public ManterViagemNegocio getMvn() {
        return mvn;
    }

    public void setMvn(ManterViagemNegocio mvn) {
        this.mvn = mvn;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public void inserir() {
        if (viagem.getKey() != null) {
            viagem.setDataRetorno(dataRetorno);
            viagem.setDataSaida(dataSaida);
            viagem.setDestino(mcn.getCidade(destino.getKey()));
            viagem.setJustificativa(justificativa);
            viagem.setMotorista(mmn.getMotorista(motorista.getKey()));
            viagem.setOrigem(mcn.getCidade(origem.getKey()));
            viagem.setStatus(status);
            viagem.setVeiculo(mven.getVeiculo(veiculo.getKey()));

            mvn.editar(viagem);
            String msge = "Sr(a) " + viagem.getMotorista().getNome() + ",\n\nUma viagem foi alterada no SIGVIAA.\n\n"
                + "Foi alterada uma viagem de " + viagem.getOrigem().getNome() + " para " + viagem.getDestino().getNome()
                + " com data de saída marcada para o dia " + converteData(dataSaida) + ", e retorno para o dia " + converteData(dataRetorno)
                + "\n\nAtenciosamente,\n\n A Direção";
            this.sendMail(viagem.getMotorista().getEmail(), msge, "SIGVIAA - Viagem Alterada");
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Viagem alterada com sucesso!", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {

            viagem.setDataRetorno(dataRetorno);
            viagem.setDataSaida(dataSaida);
            viagem.setDestino(mcn.getCidade(destino.getKey()));
            viagem.setJustificativa(justificativa);
            viagem.setMotorista(mmn.getMotorista(motorista.getKey()));
            viagem.setOrigem(mcn.getCidade(origem.getKey()));
            viagem.setStatus(status);
            viagem.setVeiculo(mven.getVeiculo(veiculo.getKey()));

            mvn.inserir(viagem);
            String msge = "Sr(a) " + viagem.getMotorista().getNome() + ",\n\nUma viagem foi cadastrada no SIGVIAA.\n\n"
                + "Foi cadastrada uma viagem de " + viagem.getOrigem().getNome() + " para " + viagem.getDestino().getNome()
                + " com data de saída marcada para o dia " + converteData(dataSaida) + ", e retorno para o dia " + converteData(dataRetorno)
                + "\n\nAtenciosamente,\n\n A Direção";
            this.sendMail(viagem.getMotorista().getEmail(), msge, "SIGVIAA - Nova viagem");
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Viagem Cadastrada Com Sucesso.", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void remover() {
        if (getSelecionada().getKey() != null) {
            mvn.remover(getSelecionada().getKey());
            String msge = "Sr(a) " + getSelecionada().getMotorista().getNome() + ",\n\nUma viagem foi cancelada no SIGVIAA.\n\n"
                + "Foi cancelada uma viagem que partia de " + getSelecionada().getOrigem().getNome() + " para " + getSelecionada().getDestino().getNome()
                + " com data de saída marcada para o dia " + converteData(getSelecionada().getDataSaida()) + ", e retorno para o dia " + converteData(getSelecionada().getDataRetorno())
                + "\n\nAtenciosamente,\n\n A Direção";
            this.sendMail(getSelecionada().getMotorista().getEmail(), msge, "SIGVIAA - Viagem Cancelada");
            this.limpar();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Viagem excluído com sucesso!", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma viagem selecionada!", null);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    private void sendMail(String email, String msg, String titulo) {
        SendMail sm = new SendMail();
        sm.sendMail("sistemas@ceresufrn.org", email, titulo, msg);
    }

    private String converteData(java.util.Date dtData) {
        SimpleDateFormat formatBra;
        formatBra = new SimpleDateFormat("dd/MM/yyyy");
        return (formatBra.format(dtData));
    }

    public List<Viagem> getViagens() {
        List<Viagem> listra = mvn.getViagens();
        return listra;
    }

    public List<Motorista> getMotoristas() {
        return mvn.getMotoristas();
    }

    public List<Veiculo> getVeiculos() {
        return mvn.getVeiculos();
    }

    public List<Cidade> getCidades() {
        return mvn.getCidades();

    }

    public int contador() {
        return mvn.contador();
    }

    public void limpar() {
        viagem = new Viagem();
        setSelecionada(new Viagem());
        carregarValoresPadrao();
    }

    public void carregarParaEditar() throws ParseException {
        viagem = getSelecionada();
        motorista = viagem.getMotorista();
        veiculo = viagem.getVeiculo();
        destino = viagem.getDestino();
        origem = viagem.getOrigem();
        status = viagem.getStatus();
        justificativa = viagem.getJustificativa();
        dataSaida = viagem.getDataSaida();
        dataRetorno = viagem.getDataRetorno();
    }

    private void carregarValoresPadrao() {

        motorista = new Motorista();
        veiculo = new Veiculo();
        destino = new Cidade();
        origem = new Cidade();
        status = "";
        justificativa = "";
        dataSaida = null;
        dataRetorno = null;
    }

    /**
     * @return the selecionada
     */
    public Viagem getSelecionada() {
        return selecionada;
    }

    /**
     * @param selecionada the selecionada to set
     */
    public void setSelecionada(Viagem selecionada) {
        this.selecionada = selecionada;
    }

    /**
     * @return the motorista
     */
    public Motorista getMotorista() {
        return motorista;
    }

    /**
     * @param motorista the motorista to set
     */
    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    /**
     * @return the veiculo
     */
    public Veiculo getVeiculo() {
        return veiculo;
    }

    /**
     * @param veiculo the veiculo to set
     */
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    /**
     * @return the destino
     */
    public Cidade getDestino() {
        return destino;
    }

    /**
     * @param destino the destino to set
     */
    public void setDestino(Cidade destino) {
        this.destino = destino;
    }

    /**
     * @return the origem
     */
    public Cidade getOrigem() {
        return origem;
    }

    /**
     * @param origem the origem to set
     */
    public void setOrigem(Cidade origem) {
        this.origem = origem;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the justificativa
     */
    public String getJustificativa() {
        return justificativa;
    }

    /**
     * @param justificativa the justificativa to set
     */
    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    /**
     * @return the dataSaida
     */
    public Date getDataSaida() {
        return dataSaida;
    }

    /**
     * @param dataSaida the dataSaida to set
     */
    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    /**
     * @return the dataRetorno
     */
    public Date getDataRetorno() {
        return dataRetorno;
    }

    /**
     * @param dataRetorno the dataRetorno to set
     */
    public void setDataRetorno(Date dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

}
