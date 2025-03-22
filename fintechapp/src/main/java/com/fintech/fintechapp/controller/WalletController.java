@RestController
@RequestMapping

public class WalletController {
    @Autowired
    private WalletService walletService;

    WalletController(FintechappApplication fintechappApplication, WalletRepository walletRepository) {
        this.fintechappApplication = fintechappApplication;
        this.walletRepository = walletRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long id) {
        Wallet wallet = walletService.getWalletById(id);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) {
        Wallet createdWallet = walletService.createWallet(wallet);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWallet);
    }
    
}