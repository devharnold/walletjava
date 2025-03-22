@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found!"));
    }
    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}