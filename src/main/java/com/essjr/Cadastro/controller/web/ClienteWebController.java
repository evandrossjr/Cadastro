package com.essjr.Cadastro.controller.web;


import com.essjr.Cadastro.model.Cliente.Cliente;
import com.essjr.Cadastro.model.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.repositories.ClienteRepository;
import com.essjr.Cadastro.services.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Controller
class ClienteWebController {


}
