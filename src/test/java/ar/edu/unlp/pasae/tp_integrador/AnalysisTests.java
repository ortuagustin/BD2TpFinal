package ar.edu.unlp.pasae.tp_integrador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import ar.edu.unlp.pasae.tp_integrador.dtos.AnalysisDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.AnalysisRequestDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.CategoricPhenotypeDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.CategoricPhenotypeValueRequestDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.NumericPhenotypeDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.NumericPhenotypeValueRequestDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.PatientRequestDTO;
import ar.edu.unlp.pasae.tp_integrador.dtos.SnpDTO;
import ar.edu.unlp.pasae.tp_integrador.entities.Analysis;
import ar.edu.unlp.pasae.tp_integrador.entities.AnalysisGroup;
import ar.edu.unlp.pasae.tp_integrador.entities.AnalysisState;
import ar.edu.unlp.pasae.tp_integrador.entities.CustomUser;
import ar.edu.unlp.pasae.tp_integrador.entities.Role;
import ar.edu.unlp.pasae.tp_integrador.entities.RoleName;
import ar.edu.unlp.pasae.tp_integrador.entities.Snp;
import ar.edu.unlp.pasae.tp_integrador.exceptions.GenotypeDecoderException;
import ar.edu.unlp.pasae.tp_integrador.repositories.AnalysisRepository;
import ar.edu.unlp.pasae.tp_integrador.repositories.CustomUserRepository;
import ar.edu.unlp.pasae.tp_integrador.repositories.RoleRepository;
import ar.edu.unlp.pasae.tp_integrador.services.AnalysisService;
import ar.edu.unlp.pasae.tp_integrador.services.CategoricPhenotypeService;
import ar.edu.unlp.pasae.tp_integrador.services.NumericPhenotypeService;
import ar.edu.unlp.pasae.tp_integrador.services.PatientService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class AnalysisTests {
	@Autowired
	private CustomUserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Autowired
	private AnalysisService analysisService;
	@Autowired
	private AnalysisRepository analysisRepository;
	@Autowired
	private CategoricPhenotypeService categoricPhenotypeService;
	@Autowired
	private NumericPhenotypeService numericPhenotypeService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	private CustomUser admin;

	@Before
	public void createUser() {
		Role rol_cientifico = new Role(RoleName.SCIENTIST.toString());
		Role rol_admin = new Role(RoleName.ADMIN.toString());
		Role rol_medico_clinico = new Role(RoleName.CLINICAL_DOCTOR.toString());
		Role rol_registrante = new Role(RoleName.REGISTER.toString());

		this.roleRepository.save(rol_cientifico);
		this.roleRepository.save(rol_admin);
		this.roleRepository.save(rol_medico_clinico);
		this.roleRepository.save(rol_registrante);

		List<Role> roles_cientifico = new ArrayList<>();
		roles_cientifico.add(rol_cientifico);

		List<Role> roles_registrante = new ArrayList<>();
		roles_registrante.add(rol_registrante);

		List<Role> roles_medico_clinico = new ArrayList<>();
		roles_registrante.add(rol_medico_clinico);

		List<Role> todos = new ArrayList<>();
		todos.add(rol_cientifico);
		todos.add(rol_admin);
		todos.add(rol_medico_clinico);
		todos.add(rol_registrante);

		this.admin = this.userRepository.save(new CustomUser("admin", passwordEncoder.encode("admin"), "admin@hotmail.com", "admin", "admin", todos));
		this.userRepository.save(new CustomUser("genaro", passwordEncoder.encode("genaro"), "genarocamele@hotmail.com", "genaro", "camele", roles_cientifico));
		this.userRepository.save(new CustomUser("agus", passwordEncoder.encode("agus"), "agus@gmail.com", "agus", "agus", roles_registrante));
		this.userRepository.save(new CustomUser("mati", passwordEncoder.encode("mati"), "mati@gmail.com", "mati", "mati", roles_medico_clinico));
	}

	@Before
	public void createPhenotypes() {
		this.createCategoricPhenotype("Adenocarcinoma", "papilar", "micropapilar", "acinar", "mucinoso", "sólido");
		this.createNumericPhenotype("Peso");
	}

	@Before
	public void createPatients() throws GenotypeDecoderException {
		Long userId = this.admin.getId();
		String name = "Agustin";
		String surname = "Ortu";
		String dni = "37058719";
		String email = "agus@ortu.com";
		String genotype = "rs111 ac\nrs1122 at";

		PatientRequestDTO request = new PatientRequestDTO(userId, name, surname, dni, email, genotype);
		request.addCategoricPhenotype(new CategoricPhenotypeValueRequestDTO(this.categoricPhenotypeId("Adenocarcinoma"), 1L));
		request.addNumericPhenotype(new NumericPhenotypeValueRequestDTO(this.numericPhenotypeId("Peso"), 50L));
		this.patientService.create(request);
	}

	private Long createCategoricPhenotype(String name, String ... values) {
		CategoricPhenotypeDTO phenotype;

		try {
			phenotype = this.categoricPhenotypeService.findByName(name);
		} catch (EntityNotFoundException e) {
			CategoricPhenotypeDTO request = new CategoricPhenotypeDTO(name, this.asSet(values));
			phenotype = this.categoricPhenotypeService.create(request);
		}

		return phenotype.getId();
	}

	private Long createNumericPhenotype(String name) {
		NumericPhenotypeDTO phenotype;

		try {
			phenotype = this.numericPhenotypeService.findByName(name);
		} catch (EntityNotFoundException e) {
			NumericPhenotypeDTO request = new NumericPhenotypeDTO(name);
			phenotype = this.numericPhenotypeService.create(request);
		}

		return phenotype.getId();
	}

	private <T> Set<T> asSet(T[] values) {
		return new HashSet<T>(Arrays.asList(values));
	}

	private Long categoricPhenotypeId(String name) {
		return this.categoricPhenotype(name).getId();
	}

	private CategoricPhenotypeDTO categoricPhenotype(String name) {
		return this.categoricPhenotypeService.findByName(name);
	}

	private Long numericPhenotypeId(String name) {
		return this.numericPhenotypeService.findByName(name).getId();
	}

	private Set<Long> patientsIds() {
		return this.patientService.list().map(patient -> patient.getId()).collect(Collectors.toSet());
	}

	@Test
	public void it_creates_new_draft_analysis_with_categoric_phenotype() throws GenotypeDecoderException {
		AnalysisRequestDTO request = new AnalysisRequestDTO("Description", this.patientsIds(), "Categoric", this.categoricPhenotypeId("Adenocarcinoma"), "rs111");
		Long analysisId = this.analysisService.create(request).getId();
		AnalysisDTO analysis = this.analysisService.find(analysisId);

		Assert.assertNotNull(analysis);
		Assert.assertNotNull(analysis.getDate());
		Assert.assertEquals("Description", analysis.getDescription());
		Assert.assertEquals(AnalysisState.PENDING, analysis.getState());
		Assert.assertEquals(this.categoricPhenotypeId("Adenocarcinoma"), analysis.getPhenotypeId());
		Assert.assertNull(analysis.getCutoffValue());
		Assert.assertEquals("Categoric", analysis.getPhenotypeKind());
	}

	@Test
	public void it_creates_new_draft_analysis_with_numeric_phenotype() throws GenotypeDecoderException {
		Long cutoffValue = 10L;

		AnalysisRequestDTO request = new AnalysisRequestDTO("Description", this.patientsIds(), "Numeric", this.numericPhenotypeId("Peso"), "rs111", cutoffValue);
		Long analysisId = this.analysisService.create(request).getId();
		AnalysisDTO analysis = this.analysisService.find(analysisId);

		Assert.assertNotNull(analysis);
		Assert.assertNotNull(analysis.getDate());
		Assert.assertEquals("Description", analysis.getDescription());
		Assert.assertEquals(AnalysisState.PENDING, analysis.getState());
		Assert.assertEquals(this.numericPhenotypeId("Peso"), analysis.getPhenotypeId());
		Assert.assertEquals("Numeric", analysis.getPhenotypeKind());
		Assert.assertEquals(cutoffValue, analysis.getCutoffValue());
	}

	@Test
	public void it_returns_one_analysis_groups_per_categoric_phenotype_value() throws GenotypeDecoderException {
		AnalysisRequestDTO request = new AnalysisRequestDTO("Description", this.patientsIds(), "Categoric", this.categoricPhenotypeId("Adenocarcinoma"), "rs333");
		Long analysisId = this.analysisService.create(request).getId();
		Analysis analysis = this.analysisRepository.findById(analysisId).get();

		Collection<AnalysisGroup> groups = analysis.getAnalysisGroups();
		Assert.assertEquals(groups.size(), this.categoricPhenotype("Adenocarcinoma").getValues().size());

		Optional<AnalysisGroup> group = analysis.getAnalysisGroup("micropapilar");
		Assert.assertTrue(group.isPresent());
		Assert.assertEquals(1, group.get().getPatients().size());
	}

	@Test
	public void it_returns_two_analysis_groups_when_using_numeric_phenotype() throws GenotypeDecoderException {
		AnalysisRequestDTO request = new AnalysisRequestDTO("Description", this.patientsIds(), "Numeric", this.numericPhenotypeId("Peso"), "rs111", 50L);
		Long analysisId = this.analysisService.create(request).getId();
		Analysis analysis = this.analysisRepository.findById(analysisId).get();

		Collection<AnalysisGroup> groups = analysis.getAnalysisGroups();
		Assert.assertEquals(groups.size(), 2);

		Optional<AnalysisGroup> highersGroup = analysis.getAnalysisGroup(">=");
		Optional<AnalysisGroup> lowersGroup = analysis.getAnalysisGroup("<");
		Assert.assertTrue(highersGroup.isPresent());
		Assert.assertTrue(lowersGroup.isPresent());
		Assert.assertEquals(1, highersGroup.get().getPatients().size());
		Assert.assertTrue(lowersGroup.get().getPatients().isEmpty());
	}

	@Test
	public void it_should_not_save_snps_when_creating_analysis_in_pending_state() throws GenotypeDecoderException {
		AnalysisRequestDTO request = new AnalysisRequestDTO("Description", this.patientsIds(), "Numeric", this.numericPhenotypeId("Peso"), "rs111", 50L);
		Long analysisId = this.analysisService.create(request).getId();
		Analysis analysis = this.analysisRepository.findById(analysisId).get();

		Assert.assertTrue(analysis.getSnps().isEmpty());
	}

	@Test
	public void it_updates_analysis_to_draft_state() throws GenotypeDecoderException {
		AnalysisRequestDTO request = new AnalysisRequestDTO("Description", this.patientsIds(), "Numeric", this.numericPhenotypeId("Peso"), "rs111", 50L);
		Long analysisId = this.analysisService.create(request).getId();
		Collection<SnpDTO> snps = new ArrayList<>();

		SnpDTO firstSnpDto = new SnpDTO("rs111", Math.random(), Math.random());
		SnpDTO secondSnpDto = new SnpDTO("rs4112", Math.random(), Math.random());

		snps.add(firstSnpDto);
		snps.add(secondSnpDto);

		this.analysisService.draft(analysisId, snps);

		Analysis analysis = this.analysisRepository.findById(analysisId).get();

		Assert.assertEquals(AnalysisState.DRAFT, analysis.getState());
		Assert.assertEquals(2, analysis.getSnps().size());

		Snp firstSnp = analysis.getSnps().stream().filter(each -> each.getSnp().equals("rs111")).findFirst().get();
		Snp secondSnp = analysis.getSnps().stream().filter(each -> each.getSnp().equals("rs4112")).findFirst().get();

		Assert.assertEquals(firstSnpDto.getSnp(), firstSnp.getSnp());
		Assert.assertEquals(firstSnpDto.getEstadistico(), firstSnp.getEstadistico());
		Assert.assertEquals(firstSnpDto.getPvalue(), firstSnp.getPvalue());

		Assert.assertEquals(secondSnpDto.getSnp(), secondSnp.getSnp());
		Assert.assertEquals(secondSnpDto.getEstadistico(), secondSnp.getEstadistico());
		Assert.assertEquals(secondSnpDto.getPvalue(), secondSnp.getPvalue());
	}
}
