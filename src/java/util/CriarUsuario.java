/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import modelo.Departamento;
import modelo.Grupo;
import modelo.Usuario;
import negocio.ManterDepartamentoNegocio;
import negocio.ManterGrupoNegocio;
import negocio.ManterUsuarioNegocio;

/**
 *
 * @author Alisson
 */
public class CriarUsuario {

    private Departamento dep = new Departamento();
    private Usuario usr = new Usuario();
    private Grupo gru = new Grupo();
    private ManterDepartamentoNegocio mdn = new ManterDepartamentoNegocio();
    private ManterUsuarioNegocio mun = new ManterUsuarioNegocio();
    private ManterGrupoNegocio mgn = new ManterGrupoNegocio();

    public CriarUsuario() {
        /*if (mdn.getDepartamentos().size() == 0) {
            dep.setNome("LABICAN");
            mdn.inserir(dep);
        }else{
            dep = mdn.getDepartamentos().get(1);
        }
        if (mgn.getGrupos().size() == 0) {
            gru.setAuthority("ROLE_ADMIN");
            gru.setNome("CHAFÂO");
            mgn.inserir(gru);
        }else{
            gru = mgn.getGrupos().get(1);
        }
        if (mun.getUsuarios().size() == 0) {
            usr.setAtivo(Boolean.TRUE);
            usr.setDepartamento(dep);
            usr.setEmail("cassio.ag@gmail.com");
            usr.setMatricula("11111111111");
            usr.setNome("Chico Bento");
            usr.setObservacao("Teste de programação");
            usr.setPassword("123");
            usr.setPermissao(gru);
            usr.setTelefone("22222222");
            usr.setUsername("administrador");
            mun.inserir(usr);
        }*/
    }
}
